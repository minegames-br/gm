package br.com.minegames.gamemanager.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Schematic;
import br.com.minegames.gamemanager.dao.ArenaDAO;

public class ArenaService extends Service {

	public ArenaService() {
		super();
	}

	public ArenaService(EntityManager em) {
		super(em);
	}

	public UUID save(Arena domain) {
		startTransaction();
		
		Area3DService aservice = new Area3DService(em);
		List<Area3D> areas = new ArrayList<Area3D>();
		
		if(domain == null) {
			System.out.println("arena is null");
			return null;
		}
		
		if(domain.getAreas() == null) {
			System.out.println("areas list is null");
		} else {
			for(Area3D area: domain.getAreas()) {
				UUID uuid = aservice.save( area );
				area = aservice.find(uuid);
				areas.add(area);
			}
		}
		
		SchematicService sservice = new SchematicService(this.em);
		Schematic schematic = domain.getSchematic();
		if(schematic.getSchematic_uuid() != null) {
			schematic = sservice.find(schematic.getSchematic_uuid());
		} else {
			UUID uuid = sservice.create(schematic);
			schematic = sservice.find(uuid);
			domain.setSchematic(schematic);
		}
		
		ArenaDAO dao = new ArenaDAO(em);
		if(domain.getArena_uuid() != null) {
			domain = dao.find(domain.getArena_uuid());
		}
		
		domain.setAreas(areas);
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
