package br.com.minegames.gamemanager.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;

import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.core.domain.GameState;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.gamemanager.dao.GameDAO;

public class GameService {

	protected EntityManagerFactory emf = Persistence.createEntityManagerFactory("game-manager"); 
	protected EntityManager em;
	
	protected void startTransaction() {
		this.em = emf.createEntityManager();
		em.getTransaction().begin();
	}
	
	public UUID createGame(Game game) {
		startTransaction();
		GameDAO dao = new GameDAO(em);
		dao.save(game);
		commitTransaction();
		LogManager.getLogger(GameService.class).info("uuid: " + game.getGame_uuid());
		return game.getGame_uuid();
	}
	
	public Game find(UUID uuid) {
		startTransaction();
		GameDAO dao = new GameDAO(em);
		Game game = dao.find(uuid);
		commitTransaction();
		return game;
	}
	
	public void commitTransaction() {
		em.getTransaction().commit();
	}
	
	public Collection<Game> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT g FROM Game g");
		Collection<Game> list = (Collection<Game>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Game game) {
		startTransaction();
		em.remove(game);
		commitTransaction();
		LogManager.getLogger(GameService.class).info("uuid: " + game.getGame_uuid() + " deletado");
	}
	
	public void startNewGame(ServerInstance server, Game game) {
		GameInstance domain = new GameInstance();
		domain.setServer(server);
		domain.setGame(game);
		domain.setStartTime(Calendar.getInstance());
		domain.setStatus(GameState.WAITING);
		startTransaction();
		em.persist(domain);
		commitTransaction();
	}
	
}
