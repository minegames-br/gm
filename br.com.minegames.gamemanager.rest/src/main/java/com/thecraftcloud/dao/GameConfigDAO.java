package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.GameConfig;

public class GameConfigDAO extends AbstractDAO {

	public GameConfigDAO(EntityManager em) {
		super(em);
	}
	
	public GameConfig find(UUID uuid) {
		return em.find(GameConfig.class, uuid);
	}
	
	public void save(GameConfig domain) {
		em.persist(domain);
	}
	

}
