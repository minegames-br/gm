package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.gamemanager.dao.Area3DDAO;

public class Area3DService extends Service {

	public UUID create(Area3D domain) {
		startTransaction();
		Area3DDAO dao = new Area3DDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(Area3DService.class).info("uuid: " + domain.getArea_uuid());
		return domain.getArea_uuid();
	}
	
	public Area3D find(UUID uuid) {
		startTransaction();
		Area3DDAO dao = new Area3DDAO(em);
		Area3D domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<Area3D> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT a FROM Area3D a");
		Collection<Area3D> list = (Collection<Area3D>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Area3D domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(Area3DService.class).info("uuid: " + domain.getArea_uuid() + " deletado");
	}
	
}
