package com.o19s.jackhanna.cli;

import java.nio.charset.Charset;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

public class EchoCommand extends AbstractCommand {
	private CuratorFramework zkClient = null;

	
	public Options getCliOptions() {
		Options options = new Options();
		Option zkPath = Option.builder().hasArg().argName("zkPath").longOpt("zkPath").required().desc("Zookeeper path").build();
		options.addOption(zkPath);

		Option path = Option.builder().hasArg().argName("t").longOpt("text").required().desc("Text to be echoed").build();
		options.addOption(path);
		return options;
	}
	
	public void doExecute(CuratorFramework client, CommandLine line) throws CommandException {
		this.zkClient = client;
		String text = line.getOptionValue("text");
		String zkPath = line.getOptionValue("zkPath");
		
		try {
			byte[] byteArray = text.getBytes(Charset.forName("UTF-8")); 
			if (zkClient.checkExists().forPath(zkPath) != null){
				zkClient.setData().forPath(zkPath, byteArray);
			}
			else {
				zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zkPath, byteArray);	
			}
		} catch (Exception e) {
			throw new CommandException("echo " + text + " to " + zkPath
					+ " failed", e);
		}
	}

	

}
