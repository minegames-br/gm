package com.thecraftcloud.core.domain;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class GameInstance extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID gi_uuid;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Game game;
	
	@OneToOne(fetch=FetchType.EAGER) 
	private ServerInstance server;

	@OneToOne(fetch=FetchType.EAGER)
	private GameWorld world;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Arena arena;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endTime;
	
	@Enumerated
	private GameState status;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch=FetchType.EAGER)
	private List<MineCraftPlayer> players;
	
	public void setGi_uuid(UUID gi_uuid) {
		this.gi_uuid = gi_uuid;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public ServerInstance getServer() {
		return server;
	}
	public void setServer(ServerInstance server) {
		this.server = server;
	}
	public Calendar getStartTime() {
		return startTime;
	}
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	public Calendar getEndTime() {
		return endTime;
	}
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	public GameState getStatus() {
		return status;
	}
	public void setStatus(GameState status) {
		this.status = status;
	}
	public UUID getGi_uuid() {
		return gi_uuid;
	}
	public GameWorld getWorld() {
		return world;
	}
	public void setWorld(GameWorld world) {
		this.world = world;
	}
	public Arena getArena() {
		return arena;
	}
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	public List<MineCraftPlayer> getPlayers() {
		return players;
	}
	public void setPlayers(List<MineCraftPlayer> players) {
		this.players = players;
	}
	
}
