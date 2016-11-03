package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Schematic;
import br.com.minegames.gamemanager.dao.SchematicDAO;

public class SchematicService extends Service {
	
	public static final String SCHEMATIC_PATH = "c:/temp";
	
	public SchematicService() {
		super();
	}
	
	public SchematicService(EntityManager em) {
		super(em);
	}

	public UUID create(Schematic domain) {
		startTransaction();
		domain.setPath(SCHEMATIC_PATH);
		SchematicDAO dao = new SchematicDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(SchematicService.class).info("uuid: " + domain.getSchematic_uuid());
		return domain.getSchematic_uuid();
	}
	
	public Schematic find(UUID uuid) {
		startTransaction();
		SchematicDAO dao = new SchematicDAO(em);
		Schematic domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<Schematic> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT gp FROM Schematic gp");
		Collection<Schematic> list = (Collection<Schematic>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Schematic domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(SchematicService.class).info("uuid: " + domain.getSchematic_uuid() + " deletado");
	}
	
}