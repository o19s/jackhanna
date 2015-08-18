package com.o19s.jackhanna.cli;


class CommandException extends Exception {
	private static final long serialVersionUID = 1L;

	CommandException() {
	}

	CommandException(String s) {
		super(s);
	}

	CommandException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
