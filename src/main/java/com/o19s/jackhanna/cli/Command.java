package com.o19s.jackhanna.cli;

import org.apache.commons.cli.Options;
import org.apache.curator.framework.CuratorFramework;




	/**
	 * A command for the zookeeper shell
	 */
	public interface Command
	{

	  /**
	   * Execute the command using the given client and the given parameters
	   */
	  int execute(CuratorFramework client, String[] cmdArgs) throws CommandException;
	  
	  /**
	   * Gets the cli options for the command
	   */
	  public Options getCliOptions();
	  
	}

