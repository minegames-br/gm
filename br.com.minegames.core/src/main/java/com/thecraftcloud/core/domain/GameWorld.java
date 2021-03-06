package com.thecraftcloud.core.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GameWorld  extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID world_uuid;	
	
	private String name;
	private String description;
	
	@OneToOne
	private Local spawn;
	
	private String path;
	
	public UUID getWorld_uuid() {
		return world_uuid;
	}
	public void setWorld_uuid(UUID world_uuid) {
		this.world_uuid = world_uuid;
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
	public Local getSpawn() {
		return spawn;
	}
	public void setSpawn(Local spawn) {
		this.spawn = spawn;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	

}
