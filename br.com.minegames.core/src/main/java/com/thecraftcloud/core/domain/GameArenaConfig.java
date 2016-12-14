package com.thecraftcloud.core.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames = {"gameconfig_game_config_uuid", "arena_arena_uuid"}))
public class GameArenaConfig extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID gac_uuid;
	
	@OneToOne(optional = false,fetch = FetchType.EAGER)
	@JoinColumn(nullable=false)
	@NotNull
	private GameConfig gameConfig;
	
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(nullable=false)
	@NotNull
	private Game game;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Arena arena;
	
	private Integer intValue;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Local localValue;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Area3D areaValue;

	@OneToOne(fetch = FetchType.EAGER)
	private Item itemValue;

	public UUID getGac_uuid() {
		return gac_uuid;
	}

	public void setGac_uuid(UUID ga_config_uuid) {
		this.gac_uuid = ga_config_uuid;
	}

	public GameConfig getGameConfig() {
		return gameConfig;
	}

	public void setGameConfig(GameConfig gameConfig) {
		this.gameConfig = gameConfig;
	}

	public Local getLocalValue() {
		return localValue;
	}

	public void setLocalValue(Local localValue) {
		this.localValue = localValue;
	}

	public Area3D getAreaValue() {
		return areaValue;
	}

	public void setAreaValue(Area3D areaValue) {
		this.areaValue = areaValue;
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public Integer getIntValue() {
		// TODO Auto-generated method stub
		return this.intValue;
	}
	
	public void setIntValue(Integer value) {
		this.intValue = value;
	}

	public Object getItemValue() {
		return this.itemValue;
	}

	public void setItemValue(Item itemValue) {
		this.itemValue = itemValue;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	
	
}
