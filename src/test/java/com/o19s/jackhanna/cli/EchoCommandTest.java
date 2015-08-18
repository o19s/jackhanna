package com.o19s.jackhanna.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.o19s.jackhanna.cli.CatCommand;
import com.o19s.jackhanna.cli.EchoCommand;

public class EchoCommandTest extends CLITestCase {

	@Test
	public void testEchoText() throws Throwable, TimeoutException, IOException {
		String[] args = { "-zkPath", ZK_PATH_ROOT + "/testme/echo/file", "-text",
				"blah=blah\nhenry=clay" };

		EchoCommand echo = new EchoCommand();
		int result = echo.execute(zkClient, args);
		assertEquals(0, result);
		
		
		ByteArrayOutputStream baos = captureStandardOut();
		String [] args2 = {"-zkPath",ZK_PATH_ROOT + "/testme/echo/file"};
		CatCommand ls = new CatCommand();
		result = ls.execute(zkClient, args2);
		resetStandardOut();
		
		assertEquals("blah=blah\nhenry=clay",baos.toString().trim());

	}

	
}
