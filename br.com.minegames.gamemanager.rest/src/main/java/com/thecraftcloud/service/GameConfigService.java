package com.thecraftcloud.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.dao.GameConfigDAO;

public class GameConfigService extends Service {

	public GameConfigService() {
		
	}
	
	public GameConfigService(EntityManager em) {
		super(em);
	}

	public UUID create(GameConfig domain) {
		startTransaction();
		GameConfigDAO dao = new GameConfigDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(GameConfigService.class).info("uuid: " + domain.getGame_config_uuid());
		return domain.getGame_config_uuid();
	}
	
	public GameConfig find(UUID uuid) {
		startTransaction();
		GameConfigDAO dao = new GameConfigDAO(em);
		GameConfig domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<GameConfig> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT gc FROM GameConfig gc");
		Collection<GameConfig> list = (Collection<GameConfig>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(GameConfig domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(GameConfigService.class).info("uuid: " + domain.getGame_config_uuid() + " deletado");
	}

	public GameConfig findByName(String name) {
		startTransaction();
		Query query = em.createQuery("SELECT gc FROM GameConfig gc where gc.name = :_name");
		query.setParameter("_name", name);
		GameConfig GameConfig = null;
		try{
			GameConfig = (GameConfig)query.getSingleResult();
		}catch(Exception e) {
			return null;
		}
		commitTransaction();
		return GameConfig;
	}
	
}
