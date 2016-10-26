package br.com.minegames.gamemanager.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.core.domain.GameState;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.gamemanager.dao.GameDAO;

public class GameService extends Service {

	public GameService() {
		super();
	}

	public GameService(EntityManager em) {
		super(em);
	}

	public UUID createGame(Game game) {
		startTransaction();
		GameDAO dao = new GameDAO(em);
		dao.save(game);
		commitTransaction();
		Logger.getLogger(GameService.class).info("uuid: " + game.getGame_uuid());
		return game.getGame_uuid();
	}
	
	public Game find(UUID uuid) {
		startTransaction();
		GameDAO dao = new GameDAO(em);
		Game game = dao.find(uuid);
		commitTransaction();
		return game;
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
		Logger.getLogger(GameService.class).info("uuid: " + game.getGame_uuid() + " deletado");
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
	
	public void addGameConfig(GameConfig gc) {
		startTransaction();
		em.persist(gc);
		commitTransaction();
	}

	public UUID createGameArenaConfig(GameArenaConfig domain) {
		startTransaction();
		
		ArenaService aservice = new ArenaService(em);
		Arena a = aservice.find(domain.getArena().getArena_uuid());
		domain.setArena(a);
		
		GameService gservice = new GameService(em);
		Game g = gservice.find(domain.getGame().getGame_uuid());
		domain.setGame(g);
		
		em.persist(domain);
		commitTransaction();
		return domain.getGa_config_uuid();
	}

	public GameArenaConfig findGameArenaConfig(UUID uuid) {
		startTransaction();
		GameArenaConfig domain = em.find(GameArenaConfig.class, uuid);
		commitTransaction();
		return domain;
	}

	public void deleteGameArenaConfig(GameArenaConfig domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
	}
	
}
