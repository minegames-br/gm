package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.MineCraftPlayer;

public class MineCraftPlayerDAO extends AbstractDAO {

	public MineCraftPlayerDAO(EntityManager em) {
		super(em);
	}
	
	public MineCraftPlayer find(UUID uuid) {
		return em.find(MineCraftPlayer.class, uuid);
	}
	
	public void save(MineCraftPlayer domain) {
		em.persist(domain);
	}
	

}
