package com.organization.projectManagement.model.request;

import javax.validation.constraints.NotNull;

public class PasswordChangeRequest {

	@NotNull(message = "Please fillout this field")
	private long userId;
	
	@NotNull(message = "Please fillout this field")
	private String password;
	
	@NotNull(message = "Please fillout this field")
	private String confirmPassword;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
