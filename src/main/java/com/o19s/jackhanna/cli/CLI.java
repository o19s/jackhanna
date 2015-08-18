package com.o19s.jackhanna.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.o19s.jackhanna.ZKSolrConfUploader;

public class CLI {
	public static final String MODULE = CLI.class.getName();

	private final static Map<String, Command> ACTIONS = new HashMap<String, Command>();

	public CLI() {

	}

	static int mainNoExit(String[] args) {
		ACTIONS.put("put", new PutCommand());
		ACTIONS.put("get", new GetCommand());
		ACTIONS.put("ls", new LsCommand());
		ACTIONS.put("cat", new CatCommand());
		ACTIONS.put("rm", new DeleteCommand());
		ACTIONS.put("echo", new EchoCommand());
		ACTIONS.put("du", new DuCommand());
		ACTIONS.put("pop", new PopCommand());
		
	    if (args.length == 0){	
	        System.out.println( "usage: java -jar jackhanna.jar zookeeper_url(localhost:2181) [commandname] [params...]");

	        for (Iterator<String> iter = ACTIONS.keySet().iterator(); iter.hasNext(); ) {
	        	String cmdName = iter.next();
	        	Command cmd = ACTIONS.get(cmdName);
	        	HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(cmdName, cmd.getCliOptions(), false );

	    	}
	        return 0;
	    }
		
		// create Options object
		Options options = new Options();
		Option server   = OptionBuilder.withArgName( "s" )
        .hasArg()
        .withDescription(  "ZooKeeper server url: 127.0.0.1:2181" )
        .create( "server" );
		server.setRequired(true);
		options.addOption(server);



		

		// this is splitting the main cmdline args from the command and its args
		// main cmdline args come first, and then the command, and then it's
		// args.
		boolean isMainArg = true;
		///String mainArgs2[] = new String()[];
		
		List<String> mainArgs = new ArrayList<String>();
		List<String> cmdArgs = new ArrayList<String>();
		for (String arg : args) {

			if (ACTIONS.keySet().contains(arg)) {
				isMainArg = false;
			}
			if (isMainArg) {
				mainArgs.add(arg);
			} else {
				cmdArgs.add(arg);
			}
		}
		
	    if (mainArgs.isEmpty() || mainArgs.get(0) == null){
	        System.err.println( "Please provide a server url as the first parameter.");
	        return -3;
	    }
		String serverUrl = mainArgs.get(0);
	    

		if (cmdArgs.size() == 0) {
			System.out.println("Allowed actions are " + ACTIONS.keySet());
			// cli.usage()
			return -2;
		}

		ZKSolrConfUploader uploader = null;
		try {
			uploader = new ZKSolrConfUploader(serverUrl, 10000, 5000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -99;
		}

		Command cmd = ACTIONS.get(cmdArgs.get(0));
		cmdArgs.remove(cmdArgs.get(0)); // remove the name of the command from the list of args
		
		try {
			cmd.execute(uploader.getZkClient(), cmdArgs.toArray(new String[cmdArgs.size()]));
		} catch (CommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			uploader.close();
		}
		return 0;
	}

	public static void main(String[] args) {
		System.exit(mainNoExit(args));
	}

}
