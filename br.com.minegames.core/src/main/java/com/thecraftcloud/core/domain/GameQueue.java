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
public class GameQueue extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID game_queue_uuid;

	@OneToOne(fetch = FetchType.EAGER)
	private Game game;

	@OneToOne(fetch = FetchType.EAGER)
	private MineCraftPlayer player;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar joinedQueue;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar leftQueue;
	
	@Enumerated
	private GameQueueStatus status;

	public UUID getGame_queue_uuid() {
		return game_queue_uuid;
	}

	public void setGame_queue_uuid(UUID game_queue_uuid) {
		this.game_queue_uuid = game_queue_uuid;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public MineCraftPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MineCraftPlayer player) {
		this.player = player;
	}

	public Calendar getJoinedQueue() {
		return joinedQueue;
	}

	public void setJoinedQueue(Calendar joinedQueue) {
		this.joinedQueue = joinedQueue;
	}

	public Calendar getLeftQueue() {
		return leftQueue;
	}

	public void setLeftQueue(Calendar leftQueue) {
		this.leftQueue = leftQueue;
	}

	public GameQueueStatus getStatus() {
		return status;
	}

	public void setStatus(GameQueueStatus status) {
		this.status = status;
	}
	
}
