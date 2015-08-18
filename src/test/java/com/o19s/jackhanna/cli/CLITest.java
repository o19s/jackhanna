package com.o19s.jackhanna.cli;

import static org.junit.Assert.*;

import org.junit.Test;

import com.o19s.jackhanna.cli.CLI;

public class CLITest  {

	@Test
	public void testPrintUsage() {
		String[] args = {};
		assertEquals(0, CLI.mainNoExit(args)); 
	}
	public void testMainWithOutEnoughParams() {

		String[] args2 = { "put" };
		assertEquals(-3, CLI.mainNoExit(args2));
		//String[] args3 = { "put", "b" };
		//assertEquals(-1, CLI.mainNoExit(args3));
		//String[] args4 = { "put", "b", "d" };
		//assertEquals(0, CLI.mainNoExit(args4));
	}
	
	public void testMainWithOutMatchingAction() {
		String[] args = { "a", "b", "d" };
		assertEquals(-2, CLI.mainNoExit(args));
	}
	
	public void testMainWithJustServer(){
		String [] args = {"localhost:2181"};
		assertEquals(-2,CLI.mainNoExit(args));
	}

}
