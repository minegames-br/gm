package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.gamemanager.dao.GameInstanceDAO;

public class GameInstanceService extends Service {
	
	public UUID create(GameInstance domain) {
		startTransaction();
		GameInstanceDAO dao = new GameInstanceDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(GameInstanceService.class).info("uuid: " + domain.getGi_uuid());
		return domain.getGi_uuid();
	}
	
	public GameInstance find(UUID uuid) {
		startTransaction();
		GameInstanceDAO dao = new GameInstanceDAO(em);
		GameInstance domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<GameInstance> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT gi FROM GameInstance gi");
		Collection<GameInstance> list = (Collection<GameInstance>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(GameInstance domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(GameInstanceService.class).info("uuid: " + domain.getGi_uuid() + " deletado");
	}
	
}
