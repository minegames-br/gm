package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.Area3D;

public class Area3DDAO extends AbstractDAO {

	public Area3DDAO(EntityManager em) {
		super(em);
	}
	
	public Area3D find(UUID uuid) {
		return em.find(Area3D.class, uuid);
	}
	
	public void save(Area3D domain) {
		em.persist(domain);
	}
	

}
