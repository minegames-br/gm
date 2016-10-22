package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Local;
import br.com.minegames.gamemanager.dao.LocalDAO;

public class LocalService extends Service {

	public UUID create(Local domain) {
		startTransaction();
		LocalDAO dao = new LocalDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(LocalService.class).info("uuid: " + domain.getLocal_uuid());
		return domain.getLocal_uuid();
	}
	
	public Local find(UUID uuid) {
		startTransaction();
		LocalDAO dao = new LocalDAO(em);
		Local domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<Local> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT l FROM Local l");
		Collection<Local> list = (Collection<Local>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Local domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(LocalService.class).info("uuid: " + domain.getLocal_uuid() + " deletado");
	}
	
}
