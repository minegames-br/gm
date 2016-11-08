package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.Game;

public class GameDAO extends AbstractDAO {

	public GameDAO(EntityManager em) {
		super(em);
	}
	
	public Game find(UUID uuid) {
		return em.find(Game.class, uuid);
	}
	
	public void save(Game game) {
		em.persist(game);
	}
	

}
