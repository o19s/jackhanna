package com.o19s.jackhanna.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class DuCommandTest extends CLITestCase {

	@Test
	public void testFailsOnBadRemotePath() throws Throwable {
		DuCommand du = new DuCommand();

		String[] args3 = {"-zkPath", ZK_PATH_ROOT + "/testme/fakepath"};
		try {
			du.doExecute(zkClient, convertArgsToCmdLine(du.getCliOptions(),args3));
			fail("My method didn't throw when I expected it to");
		} catch (CommandException expectedException) {
			assertEquals("du " + ZK_PATH_ROOT + "/testme/fakepath failed", expectedException
					.getMessage());
		}

	}	
	
	@Test
	public void testWalkTreeAndCalculateDu() throws Throwable, TimeoutException, IOException {
//		URL url = this.getClass().getResource("/src");
		File directory = new File("./src/main/resources");
		System.out.println("Directory:" + directory.getAbsolutePath());
		assertTrue(directory.exists());
		
		long spaceUsedBytes = FileUtils.sizeOfDirectory(directory.getAbsoluteFile());
		
		String[] args = {"-zkPath", ZK_PATH_ROOT + "/testgetdir","-path", directory.getAbsolutePath()};

		assertEquals(0, new PutCommand().execute(zkClient, args));
		
		String[] args2 = {"-zkPath", ZK_PATH_ROOT + "/testgetdir"};
		ByteArrayOutputStream baos = captureStandardOut();
		
		DuCommand du = new DuCommand();
		int result = du.execute(zkClient, args2);
		assertEquals(0, result);
		
		resetStandardOut();
		
		
		String zkSpaceUsed = baos.toString().trim();
		
		
		assertEquals("Verify bytes match", spaceUsedBytes+"",zkSpaceUsed);
		
		
		String[] args3 = {"-zkPath", ZK_PATH_ROOT + "/testgetdir", "-h"};
		baos = captureStandardOut();
		
		result = du.execute(zkClient, args3);
		assertEquals(0, result);
		
		resetStandardOut();
		String localSpaceUsed = FileUtils.byteCountToDisplaySize(spaceUsedBytes);
		zkSpaceUsed = baos.toString().trim();
		assertEquals("Verify human formatting works", localSpaceUsed,zkSpaceUsed);

	}

	
}
