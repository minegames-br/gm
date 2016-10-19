package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.GamePlayer;

public class PlayerDAO extends AbstractDAO {

	public PlayerDAO(EntityManager em) {
		super(em);
	}
	
	public GamePlayer find(UUID uuid) {
		return em.find(GamePlayer.class, uuid);
	}
	
	public void save(GamePlayer domain) {
		em.persist(domain);
	}
	

}
