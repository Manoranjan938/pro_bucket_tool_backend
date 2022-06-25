package com.tool.ProjectTool.model.response;

import java.math.BigInteger;

public class TeamMembersList {

	private String email;
	
	private String name;
	
	private BigInteger userid;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getUserid() {
		return userid;
	}

	public void setUserid(BigInteger userid) {
		this.userid = userid;
	}
	
}
