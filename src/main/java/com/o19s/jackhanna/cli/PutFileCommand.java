package com.o19s.jackhanna.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

public class PutFileCommand extends AbstractCommand {
	private CuratorFramework zkClient = null;


	public Options getCliOptions() {
		Options options = new Options();
		Option zkFile = Option.builder().hasArg().argName("zkFile").longOpt("zkFile").required().desc("Zookeeper path for file").build();
		options.addOption(zkFile);

		Option file = Option.builder().hasArg().argName("file").longOpt("file").required().desc("Local filesystem path for file").build();
		options.addOption(file);

		return options;
	}

	public void doExecute(CuratorFramework client, CommandLine line) throws CommandException {
		this.zkClient = client;
		String file = line.getOptionValue("file");
		String zkFile = line.getOptionValue("zkFile");

		File filePath = new File(file);
		if (!filePath.exists() || !filePath.canRead()) {
			throw new CommandException("Can't access " + filePath);
		}
		try {
			uploadToZK(filePath, zkFile);
		} catch (Exception e) {
			throw new CommandException("put " + file + " to " + zkFile
					+ " failed", e);
		}
	}

	  /**
	   * @param filePath
	   * @param zkFile
	 * @throws Exception
	   */
	  public void uploadToZK(File filePath, String zkFile) throws Exception {

		List<File> files = new ArrayList<File>();
	    if (filePath.isDirectory()) {
	    	throw new Exception("file must be a file, not a directory");

	    }


    	byte[] byteArray = new byte[(int)filePath.length()];
        byteArray = FileUtils.readFileToByteArray(filePath);
		if (zkClient.checkExists().forPath(zkFile) != null){
			zkClient.setData().forPath(zkFile, byteArray);
		}
		else {
			zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zkFile, byteArray);
		}

    
	  }

}
