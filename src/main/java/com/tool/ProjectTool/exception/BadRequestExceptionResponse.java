package com.tool.ProjectTool.exception;

public class BadRequestExceptionResponse {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BadRequestExceptionResponse(String message) {
		this.message = message;
	}
	
}
