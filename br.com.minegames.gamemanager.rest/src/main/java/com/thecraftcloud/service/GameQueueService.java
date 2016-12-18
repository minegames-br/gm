package com.thecraftcloud.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.dao.GameQueueDAO;

public class GameQueueService extends Service {

	public GameQueueService() {
		this.slave = false;
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
		boolean close = false;
		if(em == null) { 
			startTransaction();
			close = true;
		}
		GameQueueDAO dao = new GameQueueDAO(em);
		GameQueue domain = dao.find(uuid);
		if(close) commitTransaction();
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
		log("startTransaction");
		startTransaction();
		log("em.remove");
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(GameQueueService.class).info("uuid: " + domain.getGame_queue_uuid() + " deletado");
	}

	public void delete(String uuid) throws Exception {
		log("GameQueueService.delete: " + uuid);
		startTransaction();
		GameQueue domain = this.find( UUID.fromString(uuid) );
		if( domain != null) {
			log("GameQueueService.delete em.remove: " + domain);
			em.remove(domain);
		} else {
			log("GameQueue: " + uuid + " not found");
			throw new Exception("Could not remove record");
		}
		log("GameQueueService.delete commitTransaction: " + domain);
		commitTransaction();
		log("uuid: " + domain.getGame_queue_uuid() + " deletado");
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

	public void bulkStatusUpdate(GameQueueStatus oldStatus, GameQueueStatus newStatus) {
		startTransaction();
		
		logger.info("bulk status update on GameQueue: " + oldStatus + " to " + newStatus );
		
		Query q = em.createQuery("Update GameQueue gq set gq.status = :_newstatus where gq.status = :_oldstatus");
		q.setParameter("_oldstatus", oldStatus);
		q.setParameter("_newstatus", newStatus);
		q.executeUpdate();
		
		Query query = em.createQuery("SELECT gq FROM GameQueue gq");
		Collection<GameQueue> list = (Collection<GameQueue>) query.getResultList();
		for(GameQueue gq: list) {
			logger.info( "Player: " + gq.getPlayer().getName() + " Status: " + gq.getStatus().name() + " Game: " + gq.getGame().getName() );
		}
		
		commitTransaction();
	}

	public GameQueue completeGameQueueRequest(String _uuid) {
		startTransaction();
		
		GameQueueService gqService = new GameQueueService(this.em);
		GameQueue gq = gqService.find(UUID.fromString(_uuid));
		gq.setStatus(GameQueueStatus.COMPLETED);
		
		this.em.merge(gq);
		
		commitTransaction();
		return gq;
	}
	
}
