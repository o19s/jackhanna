package com.o19s.jackhanna.cli;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

public class DuCommand extends AbstractCommand {
	private CuratorFramework zkClient = null;
	private long du = 0;

	public Options getCliOptions() {
		Options options = new Options();
		
		// Not required, it can be the only parameter, passed after the ls, so not required.
		Option zkPath = Option.builder().hasArg().argName("zkPath").longOpt("zkPath").required(false).desc("Zookeeper path").build();
		options.addOption(zkPath);

		Option humanReadable = Option.builder().argName("h").longOpt("h").desc("a human-readable display value (includes units - GB, MB, KB or bytes)").build();
		options.addOption(humanReadable);
		return options;
	}

	public void doExecute(CuratorFramework client, CommandLine line)
			throws CommandException {
		this.zkClient = client;
		String zkPath = line.getOptionValue("zkPath");
		if (zkPath == null && line.getArgs().length >= 1){
			zkPath = line.getArgs()[0];
		}
		if (zkPath == null){
			throw new CommandException("Must supply either -zkPath <dir> or the <dir> following ls");
		}
		zkPath = cleanupZkPath(zkPath);
		du = 0;
		walkTree(zkPath);
		if (line.hasOption("h")) {
			System.out.print(FileUtils.byteCountToDisplaySize(du) + NEWL);
		} else {
			System.out.print(du + NEWL);
		}

	}

	public void walkTree(String zkPath) throws CommandException {
		try {

			Stat stat = zkClient.checkExists().forPath(zkPath);
			du = du + stat.getDataLength();

			List<String> children = zkClient.getChildren().forPath(zkPath);

			for (String child : children) {
				if (!child.equals("quota")) {
					walkTree(zkPath + (zkPath.equals("/") ? "" : "/") + child);
				}
			}

		} catch (Exception e) {
			throw new CommandException("du " + zkPath + " failed", e);
		}
	}

}
