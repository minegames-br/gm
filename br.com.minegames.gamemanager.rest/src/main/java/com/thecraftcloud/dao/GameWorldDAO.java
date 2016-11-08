package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.GameWorld;

public class GameWorldDAO extends AbstractDAO {

	public GameWorldDAO(EntityManager em) {
		super(em);
	}
	
	public GameWorld find(UUID uuid) {
		return em.find(GameWorld.class, uuid);
	}
	
	public void save(GameWorld domain) {
		em.persist(domain);
	}
	

}
