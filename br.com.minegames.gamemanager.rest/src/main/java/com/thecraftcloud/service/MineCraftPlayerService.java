package com.thecraftcloud.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.ServerStatus;
import com.thecraftcloud.core.domain.ServerType;
import com.thecraftcloud.dao.MineCraftPlayerDAO;

public class MineCraftPlayerService extends Service {

	public MineCraftPlayerService() {
		
	}
	
	public MineCraftPlayerService(EntityManager em) {
		super(em);
	}

	public UUID create(MineCraftPlayer domain) {
		startTransaction();
		MineCraftPlayerDAO dao = new MineCraftPlayerDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(MineCraftPlayerService.class).info("uuid: " + domain.getMcp_uuid() );
		return domain.getMcp_uuid();
	}
	
	public MineCraftPlayer find(UUID uuid) {
		startTransaction();
		MineCraftPlayerDAO dao = new MineCraftPlayerDAO(em);
		MineCraftPlayer domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<MineCraftPlayer> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT p FROM MineCraftPlayer p");
		Collection<MineCraftPlayer> list = (Collection<MineCraftPlayer>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(MineCraftPlayer domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(MineCraftPlayerService.class).info("uuid: " + domain.getMcp_uuid() + " deletado");
	}

	public MineCraftPlayer findByName(String name) {
		startTransaction();
		Query query = em.createQuery("SELECT mcp FROM MineCraftPlayer mcp where mcp.name = :_name");
		query.setParameter("_name", name);
		MineCraftPlayer MineCraftPlayer = null;
		try{
			MineCraftPlayer = (MineCraftPlayer)query.getSingleResult();
		}catch(Exception e) {
			return null;
		}
		commitTransaction();
		return MineCraftPlayer;
	}
	
	public MineCraftPlayer findByMojangUuid(String uuid) {
		startTransaction();
		Query query = em.createQuery("SELECT mcp FROM MineCraftPlayer mcp where mcp.player_uuid = :_uuid");
		query.setParameter("_uuid", uuid);
		MineCraftPlayer MineCraftPlayer = null;
		try{
			MineCraftPlayer = (MineCraftPlayer)query.getSingleResult();
		}catch(Exception e) {
			return null;
		}
		commitTransaction();
		return MineCraftPlayer;
	}

	public Collection<MineCraftPlayer> findAllPlayersNotInLobby() {
		startTransaction();
		Query query = em.createQuery("SELECT p FROM MineCraftPlayer p where p.server.type != :_serverType and p.status = :_status");
		query.setParameter("_status", PlayerStatus.ONLINE);
		query.setParameter("_serverType", ServerType.LOBBY);
		Collection<MineCraftPlayer> list = (Collection<MineCraftPlayer>) query.getResultList();
		commitTransaction();
		return list;
	}
	
}
