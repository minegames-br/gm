package br.com.minegames.gamemanager.rest;

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

import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.service.GameService;

@Path("/gamearenaconfig")
public class GameArenaConfigREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		Logger.getLogger(GameArenaConfigREST.class).info("json recebido: " + json );
		GameService service = new GameService();
		GameArenaConfig domain = (GameArenaConfig)JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		if(domain != null) {
			UUID uuid = service.createGameArenaConfig(domain);
			domain.setGa_config_uuid(uuid);
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
		Logger.getLogger(GameArenaConfigREST.class).info("uuid recebido: ");
		GameService service = new GameService();
		GameArenaConfig game = service.findGameArenaConfig( UUID.fromString(_uuid) );
		if( game != null) {
			String json = JSONParser.getInstance().toJSONString(game);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Jogo não encontrado: " + _uuid).build();
		}
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameArenaConfigREST.class).info("uuid recebido: ");
		GameService service = new GameService();
		GameArenaConfig domain = service.findGameArenaConfig( UUID.fromString(_uuid) );
		if( domain != null) {
			service.deleteGameArenaConfig(domain);
		    return Response.ok( "Configuracao Game/Arena removida com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Configuracao Game/Arena não encontrado: " + _uuid).build();
		}
	}
	
	
}
