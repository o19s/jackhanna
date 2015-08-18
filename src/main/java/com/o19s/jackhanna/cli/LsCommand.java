package com.o19s.jackhanna.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;

public class LsCommand extends AbstractCommand {

	public Options getCliOptions() {
		Options options = new Options();

		Option zkPath = OptionBuilder.withArgName("zkPath").hasArg()
				.withDescription("ZooKeeper path: /configs").create("zkPath");
		options.addOption(zkPath);
		return options;
	}
	
	public void doExecute(CuratorFramework client, CommandLine line) throws CommandException {
		String zkPath = line.getOptionValue("zkPath");
		if (zkPath == null && line.getArgs().length==1){
			zkPath = line.getArgs()[0];
		}
		if (zkPath == null){
			throw new CommandException("Must supply either -zkPath <dir> or the <dir> following ls");
		}
		try {
			List<String> children = client.getChildren().forPath(zkPath);

			for (String child : children) {
				if (!child.equals("quota")) {
					System.out.println(child);
				}
			}

		} catch (Exception e) {
			throw new CommandException("Couldn't ls path " + zkPath, e);
		}
	}

}
