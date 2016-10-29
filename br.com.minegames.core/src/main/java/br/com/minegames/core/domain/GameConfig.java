package br.com.minegames.core.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GameConfig extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID game_config_uuid;
	
	@ManyToOne
	private Game game;
	
	@Column(unique=true)
	private String name;
	
	private String groupName;
	
	private String displayName;
	
	@Enumerated
	private GameConfigType configType;
	
	@Enumerated
	private GameConfigScope configScope;

	public UUID getGame_config_uuid() {
		return game_config_uuid;
	}

	public void setGame_config_uuid(UUID game_config_uuid) {
		this.game_config_uuid = game_config_uuid;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GameConfigType getConfigType() {
		return configType;
	}

	public void setConfigType(GameConfigType configType) {
		this.configType = configType;
	}

	public GameConfigScope getConfigScope() {
		return configScope;
	}

	public void setConfigScope(GameConfigScope configScope) {
		this.configScope = configScope;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroup(String groupName) {
		this.groupName = groupName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
}
