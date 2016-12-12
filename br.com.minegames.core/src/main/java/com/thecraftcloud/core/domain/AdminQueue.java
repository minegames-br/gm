package com.thecraftcloud.core.domain;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class AdminQueue extends TransferObject implements Comparable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID uuid;	
	
	@Column( length = 255 )
	private String action;
	
	private String response;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar requestTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar responseTime;
	
	@Enumerated
	private RequestStatus status;
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Calendar getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Calendar requestTime) {
		this.requestTime = requestTime;
	}

	public Calendar getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Calendar responseTime) {
		this.responseTime = responseTime;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	@Override
	public int compareTo(Object o) {
		AdminQueue aq = (AdminQueue)o;
		return aq.getUuid().compareTo(this.getUuid());
	}
	
}
