package es.enxenio.util.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class InternalErrorException extends Exception {

	private Exception encapsulatedException;

	public InternalErrorException(Exception exception) {
		encapsulatedException = exception;
	}

	public String getMessage() {
		return encapsulatedException.getMessage();
	}

	public Exception getEncapsulatedException() {
		return encapsulatedException;
	}
	
	public void printStackTrace() {
		printStackTrace(System.err);
	}
	
	public void printStackTrace(PrintStream printStream) {
		super.printStackTrace(printStream);
		printStream.println("***Information about encapsulated exception***");
		encapsulatedException.printStackTrace(printStream);
	}
	
	public void printStackTrace(PrintWriter printWriter) {
		super.printStackTrace(printWriter);
		printWriter.println("***Information about encapsulated exception***");
		encapsulatedException.printStackTrace(printWriter);
	}
}
