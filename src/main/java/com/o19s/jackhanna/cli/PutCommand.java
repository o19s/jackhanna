package com.o19s.jackhanna.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

public class PutCommand extends AbstractCommand {
	private CuratorFramework zkClient = null;

	
	public Options getCliOptions() {
		Options options = new Options();
		Option path = OptionBuilder.withArgName("path").hasArg()
				.withDescription("Filesystem path /tmp/somefile")
				.create("path");
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
		String path = line.getOptionValue("path");
		String zkPath = line.getOptionValue("zkPath");
		
		File filePath = new File(path);
		if (!filePath.exists() || !filePath.canRead()) {
			throw new CommandException("Can't access " + filePath);
		}
		try {
			uploadToZK(filePath, zkPath);
		} catch (Exception e) {
			throw new CommandException("put " + path + " to " + zkPath
					+ " failed", e);
		}
	}

	  /**
	   * @param dir
	   * @param zkPath
	 * @throws Exception 
	   */
	  public void uploadToZK(File dir, String zkPath) throws Exception {

		List<File> files = new ArrayList<File>();
	    if (dir.isDirectory()) {
	    	files.addAll(Arrays.asList(dir.listFiles()));
	    	
	    }
	    else {
	    	files.add(dir); // we have a file.
	    }

	    for(File file : files) {
	      if (!file.getName().startsWith(".")) {
	        if (!file.isDirectory()) {
	        	byte[] byteArray = new byte[(int)file.length()];
	            byteArray = FileUtils.readFileToByteArray(file); 
	            String path = zkPath + "/" + file.getName();
				if (zkClient.checkExists().forPath(path) != null){
					zkClient.setData().forPath(path, byteArray);
				}
				else {
					zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, byteArray);	
				}

	        } else {
	          uploadToZK(file, zkPath + "/" + file.getName());
	        }
	      }
	    }
	  }

}
