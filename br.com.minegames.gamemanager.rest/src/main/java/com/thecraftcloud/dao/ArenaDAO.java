package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.Arena;

public class ArenaDAO extends AbstractDAO {

	public ArenaDAO(EntityManager em) {
		super(em);
	}
	
	public Arena find(UUID uuid) {
		return em.find(Arena.class, uuid);
	}
	
	public void save(Arena domain) {
		em.persist(domain);
	}
	

}
