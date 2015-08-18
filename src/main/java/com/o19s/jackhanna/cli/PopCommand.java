package com.o19s.jackhanna.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;

public class PopCommand extends AbstractCommand {

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
			throw new CommandException("Must supply either -zkPath <dir> or the <dir> following pop");
		}
		try {
			List<String> children = client.getChildren().forPath(zkPath);
			String pop = children.get(0);
			String[] args = { "-zkPath", zkPath + "/" + pop };

			int result = new DeleteCommand().execute(client, args);
			if (result != 0){
				throw new Exception("Couldn't delete file " + zkPath + "/" + pop + " during popping");
			}
			System.out.println(pop);

		} catch (Exception e) {
			throw new CommandException("Couldn't pop at path " + zkPath, e);
		}
	}

}
