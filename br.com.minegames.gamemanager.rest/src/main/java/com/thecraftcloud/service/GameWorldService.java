package com.thecraftcloud.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.dao.GameWorldDAO;

public class GameWorldService extends Service {

	public UUID create(GameWorld domain) {
		startTransaction();
		GameWorldDAO dao = new GameWorldDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(GameWorldService.class).info("uuid: " + domain.getWorld_uuid());
		return domain.getWorld_uuid();
	}
	
	public GameWorld find(UUID uuid) {
		startTransaction();
		GameWorldDAO dao = new GameWorldDAO(em);
		GameWorld domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<GameWorld> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT a FROM GameWorld a");
		Collection<GameWorld> list = (Collection<GameWorld>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(GameWorld domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(GameWorldService.class).info("uuid: " + domain.getWorld_uuid() + " deletado");
	}
	
}
