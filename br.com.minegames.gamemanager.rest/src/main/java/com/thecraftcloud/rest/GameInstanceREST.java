package com.thecraftcloud.rest;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.LogManager;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameInstanceService;

@Path("/gameinstance")
public class GameInstanceREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		Logger.getLogger(GameInstanceREST.class).info("json recebido: " + json );
		GameInstanceService service = new GameInstanceService();
		GameInstance domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setGi_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o GameInstance com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameInstanceREST.class).info("uuid recebido: ");
		GameInstanceService service = new GameInstanceService();
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		GameInstanceService service = new GameInstanceService();
		Collection<GameInstance> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameInstanceREST.class).info("uuid recebido: ");
		GameInstanceService service = new GameInstanceService();
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "GameInstance removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance não encontrado: " + _uuid).build();
		}
	}
	
	
}
