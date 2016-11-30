package com.thecraftcloud.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Kit;
import com.thecraftcloud.dao.KitDAO;

public class KitService extends Service {

	public KitService() {
		
	}
	
	public KitService(EntityManager em) {
		super(em);
	}

	public UUID create(Kit domain) {
		startTransaction();
		KitDAO dao = new KitDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(KitService.class).info("uuid: " + domain.getKit_uuid());
		return domain.getKit_uuid();
	}
	
	public Kit find(UUID uuid) {
		startTransaction();
		KitDAO dao = new KitDAO(em);
		Kit domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<Kit> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT k FROM Kit k");
		Collection<Kit> list = (Collection<Kit>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Kit domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(KitService.class).info("uuid: " + domain.getKit_uuid() + " deletado");
	}
	
}
