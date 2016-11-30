package com.thecraftcloud.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Schematic;
import com.thecraftcloud.core.domain.TransferObject;
import com.thecraftcloud.dao.ArenaDAO;
import com.thecraftcloud.dao.EntityManagerHelper;

public class ArenaService extends Service {

	protected boolean slave = false; 

	public ArenaService() {
		super();
	}

	public ArenaService(EntityManager em) {
		this.em = em;
		this.slave = true;
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
	
	public UUID update(Arena domain) {
		startTransaction();
		ArenaDAO dao = new ArenaDAO(em);
		Logger.getLogger(ArenaService.class).info("facing: " + domain.getFacing() );
		em.merge(domain);
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

	public Arena findArenaByName(String name) {
		startTransaction();
		Query query = em.createQuery("SELECT a FROM Arena a where a.name = :_name");
		query.setParameter("_name", name);
		Arena arena = null;
		try{
			arena = (Arena)query.getSingleResult();
		}catch(Exception e) {
			e.printStackTrace();
		}
		commitTransaction();
		return arena;
	}	

}
