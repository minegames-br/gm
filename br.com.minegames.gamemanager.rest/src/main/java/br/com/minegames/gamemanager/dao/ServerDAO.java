package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.ServerInstance;

public class ServerDAO extends AbstractDAO {

	public ServerDAO(EntityManager em) {
		super(em);
	}
	
	public ServerInstance find(UUID uuid) {
		return em.find(ServerInstance.class, uuid);
	}
	
	public void save(ServerInstance domain) {
		em.persist(domain);
	}
	

}
