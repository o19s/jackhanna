package com.o19s.jackhanna.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;

public class DeleteCommand extends AbstractCommand {
	private CuratorFramework zkClient = null;
	
	public Options getCliOptions() {
		Options options = new Options();
		Option zkPath = OptionBuilder.withArgName("zkPath").hasArg()
				.withDescription("ZooKeeper path: /configs").create("zkPath");
		zkPath.setRequired(true);
		options.addOption(zkPath);
		return options;
	}

	public void doExecute(CuratorFramework client, CommandLine line) throws CommandException {
		this.zkClient = client;
		String zkPath = line.getOptionValue("zkPath");
		if (zkPath == null && line.getArgs().length==1){
			zkPath = line.getArgs()[0];
		}
		if (zkPath == null){
			throw new CommandException("Must supply either -zkPath <dir> or the <dir> following rm");
		}
		
		try {
			tryCleanPath(zkPath);
		} catch (Exception e) {
			throw new CommandException("Couldn't delete path " + zkPath,e);
		}

	}

	public void tryCleanPath(String path) throws Exception {
		if (zkClient.checkExists().forPath(path) != null) {
//			List<String> children = zkClient.getChildren(path, null);
/*			List<String> children = zkClient.getChildren().forPath(path);
			for (String string : children) {
				tryCleanPath(path + "/" + string);
			}
			zkClient.delete(path, -1);*/
			zkClient.delete().deletingChildrenIfNeeded().forPath(path);
		}
		else {
			throw new CommandException("Path " + path + " doesn't exist.");
		}

	}

}
