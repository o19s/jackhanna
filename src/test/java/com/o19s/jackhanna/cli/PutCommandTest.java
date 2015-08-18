package com.o19s.jackhanna.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.o19s.jackhanna.cli.CommandException;
import com.o19s.jackhanna.cli.PutCommand;

public class PutCommandTest extends CLITestCase {

	@Test
	public void testFailsOnBadLocalPath() throws Throwable {
		PutCommand put = new PutCommand();

		String[] args2 = { "-path", "/fakedir", "-zkPath", ZK_PATH_ROOT };
		try {
			put.doExecute(zkClient, convertArgsToCmdLine(put.getCliOptions(),
					args2));
			fail("My method didn't throw when I expected it to");
		} catch (CommandException expectedException) {
			assertEquals("Can't access /fakedir", expectedException
					.getMessage());
		}

	}

	@Test
	public void testFailsOnBadRemotePath() throws Throwable {
		PutCommand put = new PutCommand();

		String[] args3 = { "-zkPath", ZK_PATH_ROOT + "/testme/fakepath",
				"-path", "/tmp" };
		try {
			put.doExecute(zkClient, convertArgsToCmdLine(put.getCliOptions(),
					args3));
			fail("My method didn't throw when I expected it to");
		} catch (CommandException expectedException) {
			assertEquals("put /tmp to " + ZK_PATH_ROOT + "/testme/fakepath failed",
					expectedException.getMessage());
		}

	}

	@Test
	public void testPutFile() throws Throwable, TimeoutException, IOException {
		File temp = createTempFile("put_cmd_test", "some\ntext");
		String[] args = { "-zkPath", ZK_PATH_ROOT + "/testme", "-path",
				temp.getAbsolutePath() };

		PutCommand put = new PutCommand();
		int result = put.execute(zkClient, args);
		assertEquals(0, result);

	}

	public void testPutDir() throws Throwable, TimeoutException, IOException {

		URL url = this.getClass().getResource("/solr");
		File directory = new File(url.getFile());
		assertTrue(directory.exists());

		String[] args = { "-zkPath", ZK_PATH_ROOT + "/testme2", "-path",
				directory.getAbsolutePath() };

		PutCommand put = new PutCommand();
		int result = put.execute(zkClient, args);
		assertEquals(0, result);

		// String [] args2 = {"-zkPath",ZK_PATH_ROOT + "/testme2/conf"};
		// new LsCommand().execute(zkClient, args2);

	}
}
