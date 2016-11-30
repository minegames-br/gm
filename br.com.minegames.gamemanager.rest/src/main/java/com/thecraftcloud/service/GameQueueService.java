package com.thecraftcloud.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.dao.GameQueueDAO;

public class GameQueueService extends Service {

	public GameQueueService() {
		
	}
	
	public GameQueueService(EntityManager em) {
		super(em);
	}

	public UUID create(GameQueue domain) {
		startTransaction();
		GameQueueDAO dao = new GameQueueDAO(em);
		
		domain.setJoinedQueue(Calendar.getInstance());
		domain.setStatus(GameQueueStatus.WAITING);
		
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(GameQueueService.class).info("uuid: " + domain.getGame_queue_uuid() );
		return domain.getGame_queue_uuid();
	}
	
	public GameQueue find(UUID uuid) {
		startTransaction();
		GameQueueDAO dao = new GameQueueDAO(em);
		GameQueue domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<GameQueue> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT i FROM GameQueue i");
		Collection<GameQueue> list = (Collection<GameQueue>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(GameQueue domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(GameQueueService.class).info("uuid: " + domain.getGame_queue_uuid() + " deletado");
	}

	public Collection<GameQueue> findAllGameQueueByGame(String gameName) {
		startTransaction();
		
		GameService gService = new GameService(this.em);
		
		Game game = gService.findGameByName(gameName);
		
		Query query = em.createQuery("SELECT gq FROM GameQueue gq where gq.game.game_uuid = :_game_uuid and gq.status = :status");
		query.setParameter("_game_uuid", game.getGame_uuid() );
		query.setParameter("status", GameQueueStatus.WAITING );
		Collection<GameQueue> list = (Collection<GameQueue>) query.getResultList();
		commitTransaction();
		return list;
	}

	public Collection<GameQueue> findAllGameQueueByStatus(String statusName) {
		startTransaction();
		
		GameService gService = new GameService(this.em);
		GameQueueStatus status = GameQueueStatus.valueOf(statusName);
		Query query = em.createQuery("SELECT gq FROM GameQueue gq where gq.status = :status");
		query.setParameter("status",  status );
		Collection<GameQueue> list = (Collection<GameQueue>) query.getResultList();
		commitTransaction();
		return list;
	}
	
}
