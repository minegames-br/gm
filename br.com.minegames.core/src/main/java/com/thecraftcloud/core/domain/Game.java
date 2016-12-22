package com.thecraftcloud.core.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Game extends TransferObject implements Comparable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID game_uuid;	
	
	@Column(unique=true)
	private String name;
	private String description;
	
	private String pluginName;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	private List<Kit> kit;
	
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
	public String getPluginName() {
		return pluginName;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	
	@Override
	public int compareTo(Object o) {
		Game game = (Game)o;
		return game.getGame_uuid().compareTo(this.getGame_uuid());
	}
	public List<Kit> getKit() {
		return kit;
	}
	public void setKit(List<Kit> kit) {
		this.kit = kit;
	}
	
}
