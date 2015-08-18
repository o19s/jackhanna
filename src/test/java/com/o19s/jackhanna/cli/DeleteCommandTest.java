package com.o19s.jackhanna.cli;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.o19s.jackhanna.cli.CommandException;
import com.o19s.jackhanna.cli.DeleteCommand;
import com.o19s.jackhanna.cli.PutCommand;

public class DeleteCommandTest extends CLITestCase {

	@Test
	public void testDeleteFile() throws Exception{
		File temp = createTempFile("cat_cmd_test","a\nthree\nline string");		
		
		String [] args = {"-zkPath",ZK_PATH_ROOT + "/testcat","-path", temp.getAbsolutePath()};
	
		assertEquals(0,new PutCommand().execute(zkClient, args));
		
		
		String [] args2 = {"-zkPath",ZK_PATH_ROOT + "/testcat" + "/" +  temp.getName()};
		DeleteCommand ls = new DeleteCommand();
		int result = ls.execute(zkClient, args2);
		assertEquals(0,result);
	
	}
	
	public void testDeleteDir() throws Exception{
		File temp = createTempFile("cat_cmd_test","a\nthree\nline string");		
		
		String [] args = {"-zkPath",ZK_PATH_ROOT + "/testcat","-path", temp.getAbsolutePath()};
		
		
		
		assertEquals(0,new PutCommand().execute(zkClient, args));
		
		
		String[] args2 = {"-zkPath",ZK_PATH_ROOT + "/testcat"};
		DeleteCommand ls = new DeleteCommand();
		int result = ls.execute(zkClient, args2);
		assertEquals(0,result);
		
	
	}
	
	public void testExecuteForMissingDir() throws Exception {
		String[] args = {"-zkPath",ZK_PATH_ROOT + "/totallyfakedirectory"};
		DeleteCommand cmd = new DeleteCommand();

		try {
			cmd.doExecute(zkClient, convertArgsToCmdLine(cmd.getCliOptions(),args));
		    fail( "My method didn't throw when I expected it to" );
		} catch (CommandException expectedException) {
			assertEquals("Couldn't delete path /zktest/totallyfakedirectory",expectedException.getMessage());
		}	
	}
	

}
