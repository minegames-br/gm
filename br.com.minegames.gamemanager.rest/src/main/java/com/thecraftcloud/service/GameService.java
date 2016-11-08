package com.thecraftcloud.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameState;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.exception.MineGamesException;
import com.thecraftcloud.dao.GameDAO;

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
	
	public Game updateGame(Game game) throws MineGamesException {
		startTransaction();
		GameDAO dao = new GameDAO(em);
		
		game = (Game)em.merge(game);
		try{
			commitTransaction();
		}catch(Exception e) {
			return null;
		}
		Logger.getLogger(GameService.class).info("uuid: " + game.getGame_uuid());
		return game;
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
			if(local.getLocal_uuid() == null) {
				local.setLocal_uuid( lService.create(local) );
			} else {
				local = lService.find(local.getLocal_uuid());
			}
			domain.setLocalValue(local);
		} else if(gc.getConfigType() == GameConfigType.AREA3D) {
			Area3DService aService = new Area3DService(em);
			Area3D area = domain.getAreaValue();
			if(area.getArea_uuid() == null) {
				area.setArea_uuid( aService.create(area) );
			} else {
				area = aService.find(area.getArea_uuid()); 
			}
			domain.setAreaValue(area);
		}
		if(domain.getGac_uuid() == null) {
			em.persist(domain);
		} else {
			em.merge(domain);
		}
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
		Query query = em.createQuery("SELECT gc FROM GameConfig gc where gc.game.game_uuid = :_uuid order by gc.groupName, gc.name");
		query.setParameter("_uuid", UUID.fromString(_uuid) );
		Collection<GameConfig> list = (Collection<GameConfig>) query.getResultList();
		commitTransaction();
		return list;
	}

	public GameConfig findGameConfigByName(String name) {
		GameConfig domain = null;
		startTransaction();
		Query query = em.createQuery("SELECT gc FROM GameConfig gc where gc.name = :_name order by gc.groupName, gc.name");
		query.setParameter("_name", name );
		try{
			domain = (GameConfig)query.getSingleResult();
		}catch(Exception e) {
			e.printStackTrace();
		}
		commitTransaction();
		return domain;
	}

	public Collection<GameArenaConfig> findAllGameArenaConfigByGame(UUID uuid) {
		List<GameArenaConfig> list = null;
		startTransaction();
		Query query = em.createQuery("SELECT gac FROM GameArenaConfig gac where gac.gameConfig.game.game_uuid = :_uuid");
		query.setParameter("_uuid", uuid );
		try{
			list = query.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		commitTransaction();
		return list;
	}

	public Collection<GameConfigInstance> findAllGameConfigInstanceByGame(UUID uuid) {
		List<GameConfigInstance> list = null;
		startTransaction();
		Query query = em.createQuery("SELECT gci FROM GameConfigInstance gci where gci.gameConfig.game.game_uuid = :_uuid order by gci.gameConfig.groupName, gci.gameConfig.name");
		query.setParameter("_uuid", uuid );
		try{
			list = query.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		commitTransaction();
		return list;
	}

	public Collection<GameArenaConfig> findAllGameArenaConfigByGameArena(UUID gameUUID, UUID arenaUUID) {
		List<GameArenaConfig> list = null;
		startTransaction();
		Query query = em.createQuery("SELECT gac FROM GameArenaConfig gac where gac.gameConfig.game.game_uuid = :_gameUuid and gac.arena.arena_uuid = :_arenaUuid order by gac.gameConfig.groupName, gac.gameConfig.name");
		query.setParameter("_gameUuid", gameUUID );
		query.setParameter("_arenaUuid", arenaUUID );
		try{
			list = query.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		commitTransaction();
		return list;
	}

	public GameConfigInstance findGameConfigInstanceByGameConfigUUID(UUID uuid) {
		GameConfigInstance domain = null;
		startTransaction();
		Query query = em.createQuery("SELECT gci FROM GameConfigInstance gci where gci.gameConfig.game_config_uuid = :_uuid");
		query.setParameter("_uuid", uuid );
		try{
			domain = (GameConfigInstance)query.getSingleResult();
		}catch(Exception e) {
			e.printStackTrace();
		}
		commitTransaction();
		return domain;
	}

	public UUID updateGameArenaConfig(GameArenaConfig domain) {
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
			if(local.getLocal_uuid() == null) {
				local.setLocal_uuid( lService.create(local) );
			} else {
				local = lService.find(local.getLocal_uuid());
			}
			domain.setLocalValue(local);
		} else if(gc.getConfigType() == GameConfigType.AREA3D) {
			Area3DService aService = new Area3DService(em);
			Area3D area = domain.getAreaValue();
			if(area.getArea_uuid() == null) {
				area.setArea_uuid( aService.create(area) );
			} else {
				area = aService.find(area.getArea_uuid()); 
			}
			domain.setAreaValue(area);
		}
		if(domain.getGac_uuid() == null) {
			em.persist(domain);
		} else {
			em.merge(domain);
		}
		commitTransaction();
		return domain.getGac_uuid();
	}

}
