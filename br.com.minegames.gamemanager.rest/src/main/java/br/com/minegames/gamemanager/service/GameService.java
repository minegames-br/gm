package br.com.minegames.gamemanager.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameConfigType;
import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.core.domain.GameState;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.core.exception.MineGamesException;
import br.com.minegames.gamemanager.dao.GameDAO;

public class GameService extends Service {

	public GameService() {
		super();
	}

	public GameService(EntityManager em) {
		super(em);
	}

	public UUID createGame(Game game) throws MineGamesException {
		startTransaction();
		GameDAO dao = new GameDAO(em);
		
		Game _game = this.findGameByName(game.getName());
		if(_game != null) {
			throw new MineGamesException("Game already exits with this name.");
		}
		
		game = (Game)em.merge(game);
		try{
			commitTransaction();
		}catch(Exception e) {
			return null;
		}
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

	public Game findGameByName(String name) {
		startTransaction();
		Query query = em.createQuery("SELECT g FROM Game g where g.name = :_name");
		query.setParameter("_name", name);
		Game game = null;
		try{
			game = (Game)query.getSingleResult();
		}catch(Exception e) {
			return null;
		}
		commitTransaction();
		return game;
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
	
	public GameConfig addGameConfig(GameConfig gc) {
		startTransaction();
		gc = (GameConfig)em.merge(gc);
		commitTransaction();
		return gc;
	}

	public UUID createGameArenaConfig(GameArenaConfig domain) {
		startTransaction();
		
		ArenaService aservice = new ArenaService(em);
		Arena a = aservice.find(domain.getArena().getArena_uuid());
		domain.setArena(a);
		
		GameService gservice = new GameService(em);
		GameConfig gc = em.find(GameConfig.class, domain.getGameConfig().getGame_config_uuid());
		domain.setGameConfig(gc);
		
		if(gc.getConfigType() == GameConfigType.LOCAL) {
			LocalService lService = new LocalService(em);
			Local local = domain.getLocalValue();
			local = lService.find(local.getLocal_uuid());
			domain.setLocalValue(local);
		} else if(gc.getConfigType() == GameConfigType.AREA3D) {
			Area3DService aService = new Area3DService(em);
			Area3D area = domain.getAreaValue();
			area = aService.find(area.getArea_uuid());
			domain.setAreaValue(area);
		}
		
		em.persist(domain);
		commitTransaction();
		return domain.getGac_uuid();
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

	public Collection<GameConfig> findGameConfigList(String _uuid) {
		startTransaction();
		Query query = em.createQuery("SELECT gc FROM GameConfig gc where gc.game.game_uuid = :_uuid");
		query.setParameter("_uuid", UUID.fromString(_uuid) );
		Collection<GameConfig> list = (Collection<GameConfig>) query.getResultList();
		commitTransaction();
		return list;
	}

	public GameConfig findGameConfigByName(String name) {
		GameConfig domain = null;
		startTransaction();
		Query query = em.createQuery("SELECT gc FROM GameConfig gc where gc.name = :_name");
		query.setParameter("_name", name );
		try{
			domain = (GameConfig)query.getSingleResult();
		}catch(Exception e) {
			e.printStackTrace();
		}
		commitTransaction();
		return domain;
	}

}
