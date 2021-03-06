package com.thecraftcloud.core.domain;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class MineCraftPlayer extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID mcp_uuid;
	
	@Column(unique=true)
	private String player_uuid;
	private String email;
	
	@Column(unique=true)
	private String name;
	private String nickName;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar lastLogin;

	@Enumerated
	private PlayerStatus status;
	
	@OneToOne(fetch = FetchType.EAGER)
	private ServerInstance server;
	
	public String getPlayer_uuid() {
		return player_uuid;
	}
	public void setPlayer_uuid(String player_uuid) {
		this.player_uuid = player_uuid;
	}
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public UUID getMcp_uuid() {
		return mcp_uuid;
	}
	public void setMcp_uuid(UUID mcp_uuid) {
		this.mcp_uuid = mcp_uuid;
	}
	public Calendar getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Calendar lastLogin) {
		this.lastLogin = lastLogin;
	}
	public PlayerStatus getStatus() {
		return status;
	}
	public void setStatus(PlayerStatus status) {
		this.status = status;
	}
	public ServerInstance getServer() {
		return server;
	}
	public void setServer(ServerInstance server) {
		this.server = server;
	}
	
	
}
