package com.thecraftcloud.rest;

import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameService;

@Path("/gamearenaconfig")
public class GameArenaConfigREST  extends REST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log("json recebido: " + json );
		GameService service = new GameService();
		GameArenaConfig domain = (GameArenaConfig)JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		if(domain != null) {
			UUID uuid = service.createGameArenaConfig(domain);
			domain.setGac_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("N�o � possivel criar o jogo com as informa��es fornecidas").build();
		}
	}
	
	@POST
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uuid") String _uuid, String json) {
		log("json recebido: " + json );
		GameService service = new GameService();
		
		GameArenaConfig _domain = (GameArenaConfig)service.findByUUID( GameArenaConfig.class, UUID.fromString(_uuid));
		if( _domain == null) {
			return Response.status(Response.Status.CONFLICT).entity("GameArenaConfig does not exist: " + _uuid ).build();
		}
		GameArenaConfig domain = (GameArenaConfig)JSONParser.getInstance().toObject(json, GameArenaConfig.class);
		
		if(domain != null) {
			service.updateGameArenaConfig(domain);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("N�o � possivel criar a configura��o com as informa��es fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		GameService service = new GameService();
		GameArenaConfig game = service.findGameArenaConfig( UUID.fromString(_uuid) );
		if( game != null) {
			String json = JSONParser.getInstance().toJSONString(game);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Jogo n�o encontrado: " + _uuid).build();
		}
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		log("uuid recebido: " + _uuid);
		GameService service = new GameService();
		GameArenaConfig domain = service.findGameArenaConfig( UUID.fromString(_uuid) );
		if( domain != null) {
			service.deleteGameArenaConfig(domain);
		    return Response.ok( "Configuracao Game/Arena removida com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Configuracao Game/Arena n�o encontrado: " + _uuid).build();
		}
	}
	
	
}
