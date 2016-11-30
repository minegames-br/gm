package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.Kit;

public class KitDAO extends AbstractDAO {

	public KitDAO(EntityManager em) {
		super(em);
	}
	
	public Kit find(UUID uuid) {
		return em.find(Kit.class, uuid);
	}
	
	public void save(Kit domain) {
		em.persist(domain);
	}
	

}
