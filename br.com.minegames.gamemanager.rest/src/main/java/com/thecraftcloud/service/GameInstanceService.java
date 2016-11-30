package com.thecraftcloud.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;
import com.thecraftcloud.dao.GameInstanceDAO;

public class GameInstanceService extends Service {
	
	public UUID create(GameInstance domain) {
		startTransaction();
		GameInstanceDAO dao = new GameInstanceDAO(em);
		domain.setStatus(GameState.WAITING);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(GameInstanceService.class).info("uuid: " + domain.getGi_uuid());
		return domain.getGi_uuid();
	}
	
	public GameInstance find(UUID uuid) {
		startTransaction();
		GameInstanceDAO dao = new GameInstanceDAO(em);
		GameInstance domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<GameInstance> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT gi FROM GameInstance gi");
		Collection<GameInstance> list = (Collection<GameInstance>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(GameInstance domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(GameInstanceService.class).info("uuid: " + domain.getGi_uuid() + " deletado");
	}

	public Collection<GameInstance> findAllOpenGames() {
		startTransaction();
		Query query = em.createQuery("SELECT gi FROM GameInstance gi where gi.endTime is null");

		Collection<GameInstance> list = (Collection<GameInstance>) query.getResultList();
		commitTransaction();
		return list;
	}

	public GameInstance startGameInstance(GameInstance domain) {
		startTransaction();
		domain.setStatus(GameState.RUNNING);
		if( domain.getStartTime() == null) {
			domain.setStartTime(Calendar.getInstance());
		}

		domain = em.merge(domain);
		commitTransaction();
		return domain;
	}

	public GameInstance cancelGameInstance(GameInstance domain) {
		startTransaction();
		domain.setStatus(GameState.CANCELLED);
		if( domain.getStartTime() == null) {
			domain.setStartTime(Calendar.getInstance());
		}
		domain.setEndTime(Calendar.getInstance());
		domain = em.merge(domain);
		commitTransaction();
		return domain;
	}

	public GameInstance setGameInstanceOver(GameInstance domain) {
		startTransaction();
		domain.setStatus(GameState.GAMEOVER);
		if( domain.getStartTime() == null) {
			domain.setStartTime(Calendar.getInstance());
		}
		domain.setEndTime(Calendar.getInstance());
		domain = em.merge(domain);
		commitTransaction();
		return domain;
	}

	public Collection<GameInstance> findGameInstanceAvailableByGame(String name) {
		startTransaction();
		
		GameState state = GameState.WAITING;
		
		Query query = em.createQuery("SELECT gi FROM GameInstance gi where gi.game.name = :_name and gi.status = :_status");
		query.setParameter("_name", name);
		query.setParameter("_status", state );
		GameInstance gi = null;
		Collection<GameInstance> list = (Collection<GameInstance>)query.getResultList();
		commitTransaction();
		return list;
	}
	
}
