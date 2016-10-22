package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.GameInstance;

public class GameInstanceDAO extends AbstractDAO {

	public GameInstanceDAO(EntityManager em) {
		super(em);
	}
	
	public GameInstance find(UUID uuid) {
		return em.find(GameInstance.class, uuid);
	}
	
	public void save(GameInstance domain) {
		em.persist(domain);
	}
	

}
