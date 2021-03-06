package com.thecraftcloud.core.domain;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ServerInstance extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID server_uuid;
	
	@Column(unique=true)
	private String name;
	private String description;
	private String ip_address;
	private String hostname;
	private Integer port;
	private Integer adminPort;
	
	@Enumerated
	private ServerStatus status;
	
	@Enumerated
	private ServerType type;
	
	@OneToOne
	private Local lobby;
	
	private String world;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar lastUpdate;
	
	public UUID getServer_uuid() {
		return server_uuid;
	}
	public void setServer_uuid(UUID _uuid) {
		this.server_uuid = _uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public Local getLobby() {
		return lobby;
	}
	public void setLobby(Local lobby) {
		this.lobby = lobby;
	}
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getAdminPort() {
		return adminPort;
	}
	public void setAdminPort(Integer adminPort) {
		this.adminPort = adminPort;
	}
	public ServerStatus getStatus() {
		return status;
	}
	public void setStatus(ServerStatus status) {
		this.status = status;
	}
	public Calendar getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Calendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public ServerType getType() {
		return type;
	}
	public void setType(ServerType type) {
		this.type = type;
	}
		
}
