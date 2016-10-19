package br.com.minegames.gamemanager.rest;

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

import org.apache.logging.log4j.LogManager;

import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.service.GameService;

@Path("/game")
public class GameREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		LogManager.getLogger(GameREST.class).info("json recebido: " + json );
		GameService service = new GameService();
		Game game = (Game)JSONParser.getInstance().toObject(json, Game.class);
		if(game != null) {
			UUID uuid = service.createGame(game);
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
		LogManager.getLogger(GameREST.class).info("json recebido: " + json );
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
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		LogManager.getLogger(GameREST.class).info("uuid recebido: ");
		GameService service = new GameService();
		Game game = service.find( UUID.fromString(_uuid) );
		if( game != null) {
			String json = JSONParser.getInstance().toJSONString(game);
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
		LogManager.getLogger(GameREST.class).info("uuid recebido: ");
		GameService service = new GameService();
		Game game = service.find( UUID.fromString(_uuid) );
		if( game != null) {
			service.delete(game);
		    return Response.ok( "Jogo removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Jogo não encontrado: " + _uuid).build();
		}
	}
	
	
}
