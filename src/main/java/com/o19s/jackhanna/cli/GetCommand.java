package com.o19s.jackhanna.cli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;

public class GetCommand extends AbstractCommand {
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

	public void doExecute(CuratorFramework client, CommandLine line)
			throws CommandException {
		this.zkClient = client;
		String path = line.getOptionValue("path");
		String zkPath = line.getOptionValue("zkPath");

		File filePath = new File(path);
		if (!filePath.exists() || !filePath.canWrite()) {
			throw new CommandException("Can't access " + filePath);
		}

		downloadFile(zkPath, path);

	}

	public void downloadFile(String zkPath, String path)
			throws CommandException {
		try {

			byte[] data = zkClient.getData().forPath(zkPath);
			if (data != null) {
				try {
					String dataString = new String(data, "UTF-8");

					// use buffering
					File file = new File(path
							+ zkPath.substring(zkPath.lastIndexOf("/"), zkPath
									.length()));
					Writer output = new BufferedWriter(new FileWriter(file));
					try {
						// FileWriter always assumes default encoding is OK!
						output.write(dataString);
					} finally {
						output.close();
					}
				} catch (UnsupportedEncodingException e) {
					// can't happen - UTF-8
					throw new RuntimeException(e);
				}
			}

			List<String> children = zkClient.getChildren().forPath(zkPath);
			//String zkPathChunk = zkPath.substring(zkPath.lastIndexOf("/"),
			//		zkPath.length());
			for (String child : children) {
				if (!child.equals("quota")) {
					downloadFile(zkPath + (zkPath.equals("/") ? "" : "/")
							+ child, path);
				}
			}

		} catch (Exception e) {
			throw new CommandException("get " + zkPath + " to " + path
					+ " failed", e);
		}
	}

}
