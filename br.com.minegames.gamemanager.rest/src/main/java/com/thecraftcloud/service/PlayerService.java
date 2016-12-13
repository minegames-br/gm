package com.thecraftcloud.service;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.dao.PlayerDAO;

public class PlayerService extends Service {
	
	public UUID create(MineCraftPlayer domain) {
		startTransaction();
		PlayerDAO dao = new PlayerDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(PlayerService.class).info("uuid: " + domain.getMcp_uuid());
		return domain.getMcp_uuid();
	}
	
	public MineCraftPlayer find(UUID uuid) {
		startTransaction();
		PlayerDAO dao = new PlayerDAO(em);
		MineCraftPlayer domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<MineCraftPlayer> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT gp FROM GamePlayer gp");
		Collection<MineCraftPlayer> list = (Collection<MineCraftPlayer>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(MineCraftPlayer domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(PlayerService.class).info("uuid: " + domain.getMcp_uuid() + " deletado");
	}

	public Collection<MineCraftPlayer> findAllPlayersNotInLobby() {
		startTransaction();
		Query query = em.createQuery("SELECT mcp FROM MineCraftPlayer mcp");
		Collection<MineCraftPlayer> list = (Collection<MineCraftPlayer>) query.getResultList();
		commitTransaction();
		return list;
	}
	
}
