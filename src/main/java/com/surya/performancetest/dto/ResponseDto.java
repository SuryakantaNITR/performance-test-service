package com.surya.performancetest.dto;

import java.io.Serializable;

public class ResponseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 377142091428192998L;

	private String emailId;
	
	private String uuid;
	
	public ResponseDto() {
		
	}
	
	public ResponseDto(String emailId, String uuid) {
		super();
		this.emailId = emailId;
		this.uuid = uuid;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "ResponseDto [emailId=" + emailId + ", uuid=" + uuid + "]";
	}
	
}
