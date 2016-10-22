package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Arena;
import br.com.minegames.gamemanager.dao.ArenaDAO;

public class ArenaService extends Service {

	public UUID create(Arena domain) {
		startTransaction();
		ArenaDAO dao = new ArenaDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(ArenaService.class).info("uuid: " + domain.getArena_uuid());
		return domain.getArena_uuid();
	}
	
	public Arena find(UUID uuid) {
		startTransaction();
		ArenaDAO dao = new ArenaDAO(em);
		Arena domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<Arena> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT a FROM Arena a");
		Collection<Arena> list = (Collection<Arena>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Arena domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(ArenaService.class).info("uuid: " + domain.getArena_uuid() + " deletado");
	}
	
}
