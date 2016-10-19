package br.com.minegames.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Game extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID game_uuid;	
	
	private String name;
	private String description;
	
	@OneToMany
	private List<ServerInstance> servers;
	
	public UUID getGame_uuid() {
		return game_uuid;
	}
	public void setGame_uuid(UUID game_uuid) {
		this.game_uuid = game_uuid;
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
	public List<ServerInstance> getServers() {
		return servers;
	}
	public void setServers(List<ServerInstance> servers) {
		this.servers = servers;
	}
	
	public void addServer(ServerInstance server) {
		if(servers == null) {
			servers = new ArrayList<ServerInstance>();
		}
		this.servers.add(server);
	}
	
	
}
