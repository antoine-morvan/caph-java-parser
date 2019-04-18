package org.caph.parser;

public class CaphParserException extends RuntimeException {
	private static final long serialVersionUID = 3271350602555442619L;

	public CaphParserException(final Throwable cause) {
		super(cause);
	}

	public CaphParserException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
