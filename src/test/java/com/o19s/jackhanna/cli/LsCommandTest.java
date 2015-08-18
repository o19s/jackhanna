package com.o19s.jackhanna.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Test;

import com.o19s.jackhanna.cli.LsCommand;
import com.o19s.jackhanna.cli.PutCommand;

public class LsCommandTest extends CLITestCase{

	@Test
	public void testExecute() throws Exception{
		File temp = createTempFile("cat_cmd_test","a\nthree\nline string");
		
		String[] args = {"-zkPath",ZK_PATH_ROOT + "/testls2","-path", temp.getAbsolutePath()};
		
		assertEquals(0,new PutCommand().execute(zkClient, args));
		
		
		ByteArrayOutputStream baos = captureStandardOut();
		String[] args2 = {"-zkPath",ZK_PATH_ROOT + "/testls2"};
		LsCommand ls = new LsCommand();
		int result = ls.execute(zkClient, args2);
		assertEquals(0,result);
		
		resetStandardOut();
		
		assertEquals(temp.getName(),baos.toString().trim());
	}

}
