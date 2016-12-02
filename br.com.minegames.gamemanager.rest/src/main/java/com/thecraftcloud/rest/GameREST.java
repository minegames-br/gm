package com.thecraftcloud.rest;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.exception.MineGamesException;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameService;

@Path("/game")
public class GameREST  extends REST {
	
	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findItemByName(@PathParam("name") String name) {
		Logger.getLogger(GameREST.class).info("game name: " + name);
		GameService service = new GameService();
		Game domain = service.findGameByName( name );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Game não encontrado: " + name).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		Logger.getLogger(GameREST.class).info("json recebido: " + json );
		GameService service = new GameService();
		Game game = (Game)JSONParser.getInstance().toObject(json, Game.class);
		if(game != null) {
			UUID uuid;
			try {
				uuid = service.createGame(game);
			} catch (MineGamesException e) {
				e.printStackTrace();
				return Response.status(Response.Status.CONFLICT).entity( e.getMessage() ).build();
			}
			game.setGame_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(game);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o jogo com as informações fornecidas").build();
		}
	}
	
	@Path("/start")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response startNewGame(String json) {
		Logger.getLogger(GameREST.class).info("json recebido: " + json );
		GameService service = new GameService();
		GameInstance domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
		if(domain != null) {
			service.startNewGame(domain.getServer(), domain.getGame());
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o jogo com as informações fornecidas").build();
		}
	}
	
	@Path("/config/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addGameConfig(String json) {
		Logger.getLogger(GameREST.class).info("json recebido: " + json );
		GameService service = new GameService();
		GameConfig domain = (GameConfig)JSONParser.getInstance().toObject(json, GameConfig.class);
		if(domain != null) {
			GameConfig domain2 = service.findGameConfigByName(domain.getName());
			if(domain2 == null) {
				domain = service.addGameConfig(domain);
			} else {
				domain.setGame_config_uuid(domain2.getGame_config_uuid());
				service.merge(domain);
				domain = (GameConfig)service.findByUUID( GameConfig.class, domain.getGame_config_uuid());
			}
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o config com as informações fornecidas").build();
		}
	}
	
	@Path("/config/instance/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addGameConfigInstance(String json) {
		Logger.getLogger(GameREST.class).info("json recebido: " + json );
		GameService service = new GameService();
		GameConfigInstance domain = (GameConfigInstance)JSONParser.getInstance().toObject(json, GameConfigInstance.class);
		
		log("gcInstance: " + domain );
		log("gameConfig: " + domain.getGameConfig() );
		
		log("uuid no rest: " + domain.getGameConfig().getGame_config_uuid());
		service.create(domain);
	    return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@Path("/config/instance/{uuid}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addGameConfigInstance(@PathParam("uuid") String _uuid, String json) {
		Logger.getLogger(GameREST.class).info("json recebido: " + json );
		GameService service = new GameService();
		
		GameConfigInstance _domain = service.findGameConfigInstanceByGameConfigUUID(UUID.fromString(_uuid));
		GameConfigInstance domain = (GameConfigInstance)JSONParser.getInstance().toObject(json, GameConfigInstance.class);
		if(_domain == null) {
			service.create(domain);
		} else {
			service.merge(domain);
		}
	    return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/{uuid}/config/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGameConfig(@PathParam("uuid") String _uuid) {
		GameService service = new GameService();
		Collection<GameConfig> list = service.findGameConfigList(_uuid);
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/{uuid}/config/{config_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameConfig(@PathParam("uuid") String _uuid, @PathParam("config_uuid") String _configUuid) {
		GameService service = new GameService();
		System.err.println("_game uuid: " +_uuid + " config uuid: " + _configUuid);
		GameConfig domain = (GameConfig)service.findByUUID(GameConfig.class, UUID.fromString(_configUuid) );
		String json = JSONParser.getInstance().toJSONString(domain);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGame(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameREST.class).info("uuid recebido: " + _uuid );
		GameService service = new GameService();
		Game game = service.find( UUID.fromString(_uuid) );
		if( game != null) {
			String json = JSONParser.getInstance().toJSONString(game);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Jogo não encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateGame(@PathParam("uuid") String _uuid, String json ) {
		Logger.getLogger(GameREST.class).info("uuid recebido: " + _uuid );
		GameService service = new GameService();
		Game _game = service.find( UUID.fromString(_uuid) );
		if( _game != null) {
			
			Game game = (Game)JSONParser.getInstance().toObject(json, Game.class);
			service.merge(game);
			
			json = JSONParser.getInstance().toJSONString(game);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Jogo não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		GameService service = new GameService();
		Collection<Game> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameREST.class).info("uuid recebido: ");
		GameService service = new GameService();
		Game game = service.find( UUID.fromString(_uuid) );
		if( game != null) {
			service.delete(game);
		    return Response.ok( "Jogo removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Jogo não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/{uuid}/gamearenaconfig/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGameArenaConfig(@PathParam("uuid") String _uuid) {
		GameService service = new GameService();
		Collection<GameArenaConfig> list = service.findAllGameArenaConfigByGame(UUID.fromString(_uuid));
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/{gameUuid}/gamearenaconfig/{arenaUuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGameArenaConfig(@PathParam("gameUuid") String gameUuid, @PathParam("arenaUuid") String arenaUuid) {
		GameService service = new GameService();
		Collection<GameArenaConfig> list = service.findAllGameArenaConfigByGameArena(UUID.fromString(gameUuid), UUID.fromString(arenaUuid));
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/{uuid}/gameconfiginstance/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGameConfigInstance(@PathParam("uuid") String _uuid) {
		GameService service = new GameService();
		Collection<GameConfigInstance> list = service.findAllGameConfigInstanceByGame(UUID.fromString(_uuid));
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	
}
