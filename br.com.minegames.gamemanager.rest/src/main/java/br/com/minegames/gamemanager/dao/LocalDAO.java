package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.Local;

public class LocalDAO extends AbstractDAO {

	public LocalDAO(EntityManager em) {
		super(em);
	}
	
	public Local find(UUID uuid) {
		return em.find(Local.class, uuid);
	}
	
	public void save(Local domain) {
		em.persist(domain);
	}
	

}
