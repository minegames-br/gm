package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.GameQueue;

public class GameQueueDAO extends AbstractDAO {

	public GameQueueDAO(EntityManager em) {
		super(em);
	}
	
	public GameQueue find(UUID uuid) {
		return em.find(GameQueue.class, uuid);
	}
	
	public void save(GameQueue domain) {
		em.persist(domain);
	}
	

}
