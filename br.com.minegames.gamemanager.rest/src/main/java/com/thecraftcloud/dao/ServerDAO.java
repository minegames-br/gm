package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.ServerInstance;

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
