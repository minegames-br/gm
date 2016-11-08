package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.Config;

public class ConfigDAO extends AbstractDAO {

	public ConfigDAO(EntityManager em) {
		super(em);
	}
	
	public Config find(UUID uuid) {
		return em.find(Config.class, uuid);
	}
	
	public void save(Config domain) {
		em.persist(domain);
	}
	

}
