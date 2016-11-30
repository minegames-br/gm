package com.thecraftcloud.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.dao.ItemDAO;

public class ItemService extends Service {

	public ItemService() {
		
	}
	
	public ItemService(EntityManager em) {
		super(em);
	}

	public UUID create(Item domain) {
		startTransaction();
		ItemDAO dao = new ItemDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(ItemService.class).info("uuid: " + domain.getItem_uuid());
		return domain.getItem_uuid();
	}
	
	public Item find(UUID uuid) {
		startTransaction();
		ItemDAO dao = new ItemDAO(em);
		Item domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<Item> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT i FROM Item i");
		Collection<Item> list = (Collection<Item>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Item domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(ItemService.class).info("uuid: " + domain.getItem_uuid() + " deletado");
	}

	public Item findByName(String name) {
		startTransaction();
		Query query = em.createQuery("SELECT i FROM Item i where i.name = :_name");
		query.setParameter("_name", name);
		Item item = null;
		try{
			item = (Item)query.getSingleResult();
		}catch(Exception e) {
			return null;
		}
		commitTransaction();
		return item;
	}
	
}
