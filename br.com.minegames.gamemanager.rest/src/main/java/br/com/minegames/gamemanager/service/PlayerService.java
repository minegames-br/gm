package br.com.minegames.gamemanager.service;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;

import br.com.minegames.core.domain.GamePlayer;
import br.com.minegames.gamemanager.dao.PlayerDAO;

public class PlayerService {

	protected EntityManagerFactory emf = Persistence.createEntityManagerFactory("game-manager"); 
	protected EntityManager em;
	
	protected void startTransaction() {
		this.em = emf.createEntityManager();
		em.getTransaction().begin();
	}
	
	public UUID create(GamePlayer domain) {
		startTransaction();
		PlayerDAO dao = new PlayerDAO(em);
		dao.save(domain);
		commitTransaction();
		LogManager.getLogger(PlayerService.class).info("uuid: " + domain.getGp_uuid());
		return domain.getGp_uuid();
	}
	
	public GamePlayer find(UUID uuid) {
		startTransaction();
		PlayerDAO dao = new PlayerDAO(em);
		GamePlayer domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public void commitTransaction() {
		em.getTransaction().commit();
	}
	
	public Collection<GamePlayer> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT gp FROM GamePlayer gp");
		Collection<GamePlayer> list = (Collection<GamePlayer>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(GamePlayer domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		LogManager.getLogger(PlayerService.class).info("uuid: " + domain.getGp_uuid() + " deletado");
	}
	
}
