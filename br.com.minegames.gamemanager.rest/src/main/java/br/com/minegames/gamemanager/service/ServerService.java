package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;

import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.gamemanager.dao.ServerDAO;

public class ServerService {

	protected EntityManagerFactory emf = Persistence.createEntityManagerFactory("game-manager"); 
	protected EntityManager em;
	
	protected void startTransaction() {
		this.em = emf.createEntityManager();
		em.getTransaction().begin();
	}
	
	public UUID create(ServerInstance server) {
		startTransaction();
		ServerDAO dao = new ServerDAO(em);
		dao.save(server);
		commitTransaction();
		LogManager.getLogger(ServerService.class).info("uuid: " + server.getServer_uuid());
		return server.getServer_uuid();
	}
	
	public ServerInstance find(UUID uuid) {
		startTransaction();
		ServerDAO dao = new ServerDAO(em);
		ServerInstance server = dao.find(uuid);
		commitTransaction();
		return server;
	}
	
	public void commitTransaction() {
		em.getTransaction().commit();
	}
	
	public Collection<ServerInstance> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT s FROM Server s");
		Collection<ServerInstance> list = (Collection<ServerInstance>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(ServerInstance server) {
		startTransaction();
		em.remove(server);
		commitTransaction();
		LogManager.getLogger(ServerService.class).info("uuid: " + server.getServer_uuid() + " deletado");
	}
	
}
