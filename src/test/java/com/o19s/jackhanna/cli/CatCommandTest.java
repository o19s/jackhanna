package com.o19s.jackhanna.cli;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Test;

import com.o19s.jackhanna.cli.CatCommand;
import com.o19s.jackhanna.cli.PutCommand;

public class CatCommandTest extends CLITestCase {

	@Test
	public void testExecute() throws Exception{
		File temp = createTempFile("cat_cmd_test","a\nthree\nline string");		

		String[] args = {"-zkPath",ZK_PATH_ROOT + "/testcat","-path", temp.getAbsolutePath()};
		
		
		assertEquals(0,new PutCommand().execute(zkClient, args));
		
		ByteArrayOutputStream baos = captureStandardOut();
		

		String [] args2 = {"-zkPath",ZK_PATH_ROOT + "/testcat" + "/" +  temp.getName()};
		CatCommand ls = new CatCommand();
		int result = ls.execute(zkClient, args2);
		assertEquals(0,result);
		
		resetStandardOut();
		
		assertEquals("a\nthree\nline string",baos.toString().trim());
	}

}
