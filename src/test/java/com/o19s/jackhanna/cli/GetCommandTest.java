package com.o19s.jackhanna.cli;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.o19s.jackhanna.cli.CommandException;
import com.o19s.jackhanna.cli.GetCommand;
import com.o19s.jackhanna.cli.PutCommand;

public class GetCommandTest extends CLITestCase {

	@Test
	public void testFailsOnBadLocalPath() throws Throwable {
		GetCommand get = new GetCommand();
	
		String[] args2 = {"-path","/fakedir", "-zkPath", ZK_PATH_ROOT};
		try {
			get.doExecute(zkClient, convertArgsToCmdLine(get.getCliOptions(),args2));
			fail("My method didn't throw when I expected it to");
		} catch (CommandException expectedException) {
			assertEquals("Can't access /fakedir", expectedException
					.getMessage());
		}
	

	}
	
	@Test
	public void testFailsOnBadRemotePath() throws Throwable {
		GetCommand get = new GetCommand();

		String[] args3 = {"-zkPath", ZK_PATH_ROOT + "/testme/fakepath","-path", "/tmp"};
		try {
			get.doExecute(zkClient, convertArgsToCmdLine(get.getCliOptions(),args3));
			fail("My method didn't throw when I expected it to");
		} catch (CommandException expectedException) {
			assertEquals("get " + ZK_PATH_ROOT + "/testme/fakepath to /tmp failed", expectedException
					.getMessage());
		}

	}	

	@Test
	public void OfftestGetFile() throws Throwable, TimeoutException, IOException {
		File temp = createTempFile("put_cmd_test", "some\ntext");
		String[] args = {"-zkPath", ZK_PATH_ROOT + "/testgetfile","-path", temp.getAbsolutePath()};

		assertEquals(0, new PutCommand().execute(zkClient, args));

		File tempDir = createTempDir();
		
		String[] args2 = {"-zkPath", ZK_PATH_ROOT + "/testgetfile" + "/" + temp.getName(),"-path", tempDir.getAbsolutePath()};

		GetCommand get = new GetCommand();
		int result = get.execute(zkClient, args2);
		assertEquals(0, result);
		
		String fileContents = GetCommandTest.readFile(tempDir.getAbsolutePath() + "/" + temp.getName());
		assertEquals("some\ntext",fileContents.trim());

	}

	@Test
	public void testGetDir() throws Throwable, TimeoutException, IOException {
		URL url = this.getClass().getResource("/");
		File directory = new File(url.getFile());
		assertTrue(directory.exists());
		String[] args = {"-zkPath", ZK_PATH_ROOT + "/testgetdir","-path", directory.getAbsolutePath()};

		assertEquals(0, new PutCommand().execute(zkClient, args));
		
		File tempDir = createTempDir();
		String[] args2 = {"-zkPath", ZK_PATH_ROOT + "/testgetdir","-path", tempDir.getAbsolutePath()};

		GetCommand get = new GetCommand();
		int result = get.execute(zkClient, args2);
		assertEquals(0, result);
		
		//String fileContents = GetCommandTest.readFile(tempDir.getAbsolutePath() + "/" + temp.getName());
		//assertEquals("some\ntext",fileContents.trim());
		
		

	}

	private static String readFile(String filename) throws IOException {
		String lineSep = System.getProperty("line.separator");
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String nextLine = "";
		StringBuffer sb = new StringBuffer();
		while ((nextLine = br.readLine()) != null) {
			sb.append(nextLine);
			//
			// note:
			// BufferedReader strips the EOL character
			// so we add a new one!
			//
			sb.append(lineSep);
		}
		return sb.toString();
	}
}
