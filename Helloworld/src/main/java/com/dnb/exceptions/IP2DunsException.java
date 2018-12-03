package com.dnb.exceptions;

public class IP2DunsException extends Exception {
	private static final long serialVersionUID = 1L;

	public IP2DunsException() {

	}

	public IP2DunsException(String message) {
		super(message);

	}

	public IP2DunsException(Throwable cause) {
		super(cause);

	}

	public IP2DunsException(String message, Throwable cause) {
		super(message, cause);

	}

	public IP2DunsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}
	

}
