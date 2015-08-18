package com.o19s.jackhanna.cli;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;

import com.o19s.jackhanna.ZKSolrConfUploader;

public abstract class CLITestCase {

	protected ZKSolrConfUploader uploader;
	protected CuratorFramework zkClient;

	private PrintStream out;

	protected String ZK_PATH_ROOT = "/zktest";

	@Before
	public void setUp() {
		try {
			int standaloneZKPort = 2181;
			uploader = new ZKSolrConfUploader("localhost:" + standaloneZKPort, 10000, 5000);
			zkClient = uploader.getZkClient();

			String[] args = { "-zkPath", ZK_PATH_ROOT };

			int result = new DeleteCommand().execute(zkClient, args);
			if (result != 0 && result != 1) {
				throw new Exception("Result was " + result
						+ ", something went wrong");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@After
	public void tearDown() {
		if (uploader != null) {
			uploader.close();
		}
	}

	public CommandLine convertArgsToCmdLine(Options options, String[] args)
			throws RuntimeException {
		CommandLineParser parser = new DefaultParser();
		CommandLine line = null;

		try {
			line = parser.parse(options, args);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return line;
	}

	public File createTempFile(String fileName, String contents) {
		File temp = null;
		try {
			// Create temp file.
			temp = File.createTempFile(fileName, ".txt");

			// Delete temp file when program exits.
			temp.deleteOnExit();

			// Write to temp file
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			out.write(contents);
			out.close();
		} catch (IOException e) {
		}
		return temp;
	}

	public File createTempDir() {
		File temp = null;
		// Create temp file.
		temp = new File("/tmp/" + "temp" + Long.toString(System.nanoTime()));

		// Delete temp file when program exits.
		// temp.deleteOnExit();
		if (!(temp.mkdir())) {
			throw new RuntimeException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}
		return (temp);
	}

	public ByteArrayOutputStream captureStandardOut() {
		out = System.out;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream capturedStandardOut = new PrintStream(baos);
		System.setOut(capturedStandardOut);
		return baos;
	}

	public void resetStandardOut() {
		System.setOut(out);
	}
}
