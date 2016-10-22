package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.Arena;

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
