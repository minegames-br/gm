package com.thecraftcloud.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.dao.ServerDAO;

public class ServerService extends Service {
	
	public UUID create(ServerInstance server) {
		startTransaction();
		ServerDAO dao = new ServerDAO(em);
		dao.save(server);
		commitTransaction();
		Logger.getLogger(ServerService.class).info("uuid: " + server.getServer_uuid());
		return server.getServer_uuid();
	}
	
	public ServerInstance find(UUID uuid) {
		startTransaction();
		ServerDAO dao = new ServerDAO(em);
		ServerInstance server = dao.find(uuid);
		commitTransaction();
		return server;
	}
	
	public Collection<ServerInstance> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT si FROM ServerInstance si");
		Collection<ServerInstance> list = (Collection<ServerInstance>) query.getResultList();
		commitTransaction();
		return list;
	}

	public Collection<ServerInstance> findAllOnline() {
		startTransaction();
		Query query = em.createQuery("SELECT si FROM ServerInstance si ");
		Collection<ServerInstance> list = (Collection<ServerInstance>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(ServerInstance server) {
		startTransaction();
		em.remove(server);
		commitTransaction();
		Logger.getLogger(ServerService.class).info("uuid: " + server.getServer_uuid() + " deletado");
	}

	public ServerInstance findByName(String name) {
		startTransaction();
		Query query = em.createQuery("SELECT s FROM ServerInstance s where s.name = :_name");
		query.setParameter("_name", name);
		ServerInstance server = null;
		try{
			server = (ServerInstance)query.getSingleResult();
		}catch(Exception e) {
			return null;
		}
		commitTransaction();
		return server;
	}
	
}
