package com.learntechpuzz.lambda.exception;

public class InternalErrorException extends Exception {
	private static final String PREFIX = "500: ";

	public InternalErrorException(String s, Exception e) {
		super(PREFIX + s, e);
	}

	public InternalErrorException(String s) {
		super(PREFIX + s);
	}

}
