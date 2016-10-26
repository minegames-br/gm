package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Local;
import br.com.minegames.gamemanager.dao.Area3DDAO;
import br.com.minegames.gamemanager.dao.LocalDAO;

public class Area3DService extends Service {

	public Area3DService() {
		
	}

	public Area3DService(EntityManager em) {
		super(em);
	}

	public UUID create(Area3D domain) {
		startTransaction();
		System.out.println("Save first pointA and B");
		LocalDAO ldao = new LocalDAO(em);
		/*
		ldao.save(domain.getPointA());
		Local l = ldao.find(domain.getPointA().getLocal_uuid());
		domain.setPointA(l);
		ldao.save(domain.getPointB());
		l = ldao.find(domain.getPointB().getLocal_uuid());
		domain.setPointB(l);
*/		
		Local pointA = domain.getPointA();
		ldao.save(pointA);
		Local pointB = domain.getPointB();
		ldao.save(pointB);
		domain.setPointA(pointA);
		domain.setPointB(pointB);
		
		System.out.println("Now save Area3D");
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

	public UUID save(Area3D domain) {
		startTransaction();
		System.out.println("Save first pointA and B");
		LocalDAO ldao = new LocalDAO(em);

		Local pointA = domain.getPointA();
		ldao.save(pointA);
		Local pointB = domain.getPointB();
		ldao.save(pointB);
		domain.setPointA(pointA);
		domain.setPointB(pointB);
		
		System.out.println("Now save Area3D");
		Area3DDAO dao = new Area3DDAO(em);
		dao.save(domain);
		
		commitTransaction();
		Logger.getLogger(Area3DService.class).info("uuid: " + domain.getArea_uuid());
		return domain.getArea_uuid();
	}
	
}
