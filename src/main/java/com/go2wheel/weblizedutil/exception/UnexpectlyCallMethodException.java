package com.go2wheel.weblizedutil.exception;

public class UnexpectlyCallMethodException extends HasErrorIdAndMsgkey {

	public UnexpectlyCallMethodException(String className, String methodName) {
		super("100002", "exception.unexpected-called", "The method be called Unexpectly", new Object[] {className, methodName});
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
