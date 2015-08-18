package com.o19s.jackhanna.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;

public class DeleteCommand extends AbstractCommand {
	private CuratorFramework zkClient = null;
	
	public Options getCliOptions() {
		Options options = new Options();
		
		// Not required, it can be the only parameter, passed after the ls, so not required.
		Option zkPath = Option.builder().hasArg().argName("zkPath").longOpt("zkPath").required(false).desc("Zookeeper path").build();
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
		
		zkPath = cleanupZkPath(zkPath);
		
		try {
			if (zkClient.checkExists().forPath(zkPath) != null) {

				zkClient.delete().deletingChildrenIfNeeded().forPath(zkPath);
			}
			else {
				System.err.println("rm: " + zkPath + ": No such node");
			}
		} catch (Exception e) {
			throw new CommandException("Delete of " + zkPath +" failed",e);
		}

	}



}
