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

import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameInstanceService;
import com.thecraftcloud.service.ItemService;

@Path("/gameinstance")
public class GameInstanceREST  extends REST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log("json recebido: " + json );
		GameInstanceService service = new GameInstanceService();
		GameInstance domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setGi_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("N�o � possivel criar o GameInstance com as informa��es fornecidas").build();
		}
	}
	
	@POST
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uuid") String _uuid, String json) {
		GameInstanceService service = new GameInstanceService();
		log("json: " + json);
		log("json: " + json);
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
			service.merge(domain);
			domain = service.find( UUID.fromString(_uuid) );
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance n�o encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{uuid}/cancel")
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelGameInstance(@PathParam("uuid") String _uuid, String json) {
		GameInstanceService service = new GameInstanceService();
		log("json: " + json);
		log("json: " + json);
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
			domain = service.cancelGameInstance(domain);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance n�o encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{uuid}/over")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setGameOver(@PathParam("uuid") String _uuid, String json) {
		GameInstanceService service = new GameInstanceService();
		log("json: " + json);
		log("json: " + json);
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
			domain = service.setGameInstanceOver(domain);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance n�o encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{uuid}/start")
	@Produces(MediaType.APPLICATION_JSON)
	public Response startGameInstance(@PathParam("uuid") String _uuid, String json) {
		GameInstanceService service = new GameInstanceService();
		log("json: " + json);
		log("json: " + json);
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			domain = (GameInstance)JSONParser.getInstance().toObject(json, GameInstance.class);
			domain = service.startGameInstance(domain);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance n�o encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		GameInstanceService service = new GameInstanceService();
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance n�o encontrado: " + _uuid).build();
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
	
	@GET
	@Path("/list/open")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllOpenGames() {
		GameInstanceService service = new GameInstanceService();
		Collection<GameInstance> list = service.findAllOpenGames();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		GameInstanceService service = new GameInstanceService();
		GameInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "GameInstance removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameInstance n�o encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameInstanceAvailableByName(@PathParam("name") String name) {
		log("game recebido: " + name);
		
		GameInstanceService giService = new GameInstanceService();
		Collection<GameInstance> list = giService.findGameInstanceAvailableByGame( name ); 
		
		if( list != null) {
			String json = JSONParser.getInstance().toJSONString(list);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Nenhum GameInstance disponivel encontrado: " + name).build();
		}
	}
	
	
}
