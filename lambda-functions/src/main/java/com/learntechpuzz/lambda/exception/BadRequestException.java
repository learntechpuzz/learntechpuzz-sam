package com.learntechpuzz.lambda.exception;

public class BadRequestException extends Exception {
	private static final String PREFIX = "400: ";

	public BadRequestException(String s, Exception e) {
		super(PREFIX + s, e);
	}

	public BadRequestException(String s) {
		super(PREFIX + s);
	}
}
