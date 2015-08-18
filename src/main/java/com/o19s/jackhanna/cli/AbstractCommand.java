package com.o19s.jackhanna.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.curator.framework.CuratorFramework;

public abstract class AbstractCommand implements Command {
	static final String NEWL = System.getProperty("line.separator");
	/**
	 * Executes the actual command
	 */

	public int execute(CuratorFramework client, String[] cmdArgs){
		Options options = getCliOptions();

		//CommandLineParser parser = new GnuParser();
		CommandLineParser parser = new PosixParser();
		CommandLine line = null;

		try {
			// parse the command line arguments
			line = parser.parse(options, cmdArgs);

		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "zk", options, true );
			return -2;
		}

		try {
			doExecute(client, line);
			return 0;
		} catch (Throwable th) {			
			System.err.println(th.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "zk", options, true );
			return 1;
		}
	}

	public abstract void doExecute(CuratorFramework client, CommandLine line)
			throws CommandException;

}
