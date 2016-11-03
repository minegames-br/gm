package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.MineCraftPlayer;

public class PlayerDAO extends AbstractDAO {

	public PlayerDAO(EntityManager em) {
		super(em);
	}
	
	public MineCraftPlayer find(UUID uuid) {
		return em.find(MineCraftPlayer.class, uuid);
	}
	
	public void save(MineCraftPlayer domain) {
		em.persist(domain);
	}
	

}
