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

import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameConfigService;

@Path("/gameconfig")
public class GameConfigREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		Logger.getLogger(GameConfigREST.class).info("json recebido: " + json );
		GameConfigService service = new GameConfigService();
		GameConfig domain = (GameConfig)JSONParser.getInstance().toObject(json, GameConfig.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setGame_config_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o GameConfig com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameConfigREST.class).info("uuid recebido: ");
		GameConfigService service = new GameConfigService();
		GameConfig domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameConfig não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameConfigByName(@PathParam("name") String name) {
		System.out.println("name: " + name);
		GameConfigService service = new GameConfigService();
		GameConfig domain = service.findByName( name );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameConfig não encontrado: " + name).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		GameConfigService service = new GameConfigService();
		Collection<GameConfig> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameConfigREST.class).info("uuid recebido: ");
		GameConfigService service = new GameConfigService();
		GameConfig domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "GameConfig removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameConfig não encontrado: " + _uuid).build();
		}
	}
	
	
}
