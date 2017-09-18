package com.createvissualmedia.web.service.exception;

/**
 * 
 * @author prasadprabhakaran
 *
 */
public class ServiceException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2737638714358347945L;
	private String errorCode;
	private String message;
	
	public ServiceException(String errorCode, String message){
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
