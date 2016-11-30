package com.thecraftcloud.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.Item;

public class ItemDAO extends AbstractDAO {

	public ItemDAO(EntityManager em) {
		super(em);
	}
	
	public Item find(UUID uuid) {
		return em.find(Item.class, uuid);
	}
	
	public void save(Item domain) {
		em.persist(domain);
	}
	

}
