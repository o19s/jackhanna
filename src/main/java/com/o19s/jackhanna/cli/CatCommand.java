package com.o19s.jackhanna.cli;

import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;

public class CatCommand extends AbstractCommand {
	

	public Options getCliOptions() {
		Options options = new Options();
		
		// Not required, it can be the only parameter, passed after the ls, so not required.
		Option zkPath = Option.builder().hasArg().argName("zkPath").longOpt("zkPath").required(false).desc("Zookeeper path").build();
		options.addOption(zkPath);
		return options;
	}

	public void doExecute(CuratorFramework client, CommandLine line)
			throws CommandException {

		String zkPath = line.getOptionValue("zkPath");
		if (zkPath == null && line.getArgs().length==1){
			zkPath = line.getArgs()[0];
		}
		if (zkPath == null){
			throw new CommandException("Must supply either -zkPath <file> or a paramter following cat");
		}	
		zkPath = cleanupZkPath(zkPath);
		try {
			byte[] data = client.getData().forPath(zkPath);
			if (data != null) {
				try {
					String dataString = new String(data, "UTF-8");
					System.out.print(dataString + NEWL);

				} catch (UnsupportedEncodingException e) {
					// can't happen - UTF-8
					throw new RuntimeException(e);
				}
			}

		} catch (Exception e) {
			throw new CommandException("Couldn't print layout", e);
		}

	}

}
