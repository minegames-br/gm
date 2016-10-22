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

import org.jboss.logging.Logger;

import br.com.minegames.core.domain.GameWorld;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.service.GameWorldService;

@Path("/world")
public class GameWorldREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		Logger.getLogger(GameWorldREST.class).info("json recebido: " + json );
		GameWorldService service = new GameWorldService();
		GameWorld domain = (GameWorld)JSONParser.getInstance().toObject(json, GameWorld.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setWorld_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o GameWorld com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameWorldREST.class).info("uuid recebido: ");
		GameWorldService service = new GameWorldService();
		GameWorld domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameWorld não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		GameWorldService service = new GameWorldService();
		Collection<GameWorld> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		Logger.getLogger(GameWorldREST.class).info("uuid recebido: ");
		GameWorldService service = new GameWorldService();
		GameWorld domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "GameWorld removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Local não encontrado: " + _uuid).build();
		}
	}
	
	
}
