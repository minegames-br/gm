package br.com.minegames.gamemanager.dao;

import java.util.UUID;

import javax.persistence.EntityManager;

import br.com.minegames.core.domain.Schematic;

public class SchematicDAO extends AbstractDAO {

	public SchematicDAO(EntityManager em) {
		super(em);
	}
	
	public Schematic find(UUID uuid) {
		return em.find(Schematic.class, uuid);
	}
	
	public void save(Schematic domain) {
		em.persist(domain);
	}
	

}
