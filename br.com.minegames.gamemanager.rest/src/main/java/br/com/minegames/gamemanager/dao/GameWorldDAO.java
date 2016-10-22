package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.GameWorld;

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
