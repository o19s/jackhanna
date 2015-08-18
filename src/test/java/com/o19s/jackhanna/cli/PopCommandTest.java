package com.o19s.jackhanna.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Test;

import com.o19s.jackhanna.cli.PopCommand;
import com.o19s.jackhanna.cli.PutCommand;

public class PopCommandTest extends CLITestCase {

	@Test
	public void testExecute() throws Exception{
		File temp = createTempFile("cat_cmd_test","a\nthree\nline string");		

		String[] args = {"-zkPath",ZK_PATH_ROOT + "/testcat","-path", temp.getAbsolutePath()};
		
		
		assertEquals(0,new PutCommand().execute(zkClient, args));
		
		
		File temp2 = createTempFile("cat_cmd_test","a\nthree\nline string");		

		String[] args2 = {"-zkPath",ZK_PATH_ROOT + "/testcat","-path", temp2.getAbsolutePath()};
		
		
		assertEquals(0,new PutCommand().execute(zkClient, args));
		
		
		ByteArrayOutputStream baos = captureStandardOut();
		String [] args3 = {"-zkPath",ZK_PATH_ROOT + "/testcat"};
		PopCommand pop = new PopCommand();
		int result = pop.execute(zkClient, args3);
		assertEquals(0,result);
		
		resetStandardOut();
		

		
		
	}

}
