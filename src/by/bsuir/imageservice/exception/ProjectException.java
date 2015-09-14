package by.bsuir.imageservice.exception;

public class ProjectException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Exception hiddenException;

	public ProjectException(String msg) {
		super(msg);
	}

	public ProjectException(String msg, Exception e) {
		super(msg);
		hiddenException = e;
	}

	public ProjectException(Throwable cause) {
		super(cause);
	}

	public Exception getHiddenException() {
		return hiddenException;
	}

}
