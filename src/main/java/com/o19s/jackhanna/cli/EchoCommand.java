package com.o19s.jackhanna.cli;

import java.nio.charset.Charset;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

public class EchoCommand extends AbstractCommand {
	private CuratorFramework zkClient = null;

	
	public Options getCliOptions() {
		Options options = new Options();
		Option path = OptionBuilder.withArgName("text").hasArg()
				.withDescription("Text to be echoed.")
				.create("text");
		path.setRequired(true);
		options.addOption(path);
		Option zkPath = OptionBuilder.withArgName("zkPath").hasArg()
				.withDescription("ZooKeeper path: /configs").create("zkPath");
		zkPath.setRequired(true);
		options.addOption(zkPath);
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
