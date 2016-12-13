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

import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameService;
import com.thecraftcloud.service.MineCraftPlayerService;

@Path("/player")
public class MineCraftPlayerREST extends REST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log("json recebido: " + json );
		MineCraftPlayerService service = new MineCraftPlayerService();
		MineCraftPlayer domain = (MineCraftPlayer)JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setMcp_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o MineCraftPlayer com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		MineCraftPlayerService service = new MineCraftPlayerService();
		MineCraftPlayer domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("MineCraftPlayer não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findMineCraftPlayerByName(@PathParam("name") String name) {
		log("uuid recebido: ");
		MineCraftPlayerService service = new MineCraftPlayerService();
		MineCraftPlayer domain = service.findByName( name );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("MineCraftPlayer não encontrado: " + name).build();
		}
	}
	
	@GET
	@Path("/mojanguuid/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findMineCraftPlayerByMojangUUID(@PathParam("uuid") String uuid) {
		log("uuid recebido: " + uuid);
		MineCraftPlayerService service = new MineCraftPlayerService();
		MineCraftPlayer domain = service.findByMojangUuid( uuid );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("MineCraftPlayer não encontrado: " + uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		MineCraftPlayerService service = new MineCraftPlayerService();
		Collection<MineCraftPlayer> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/notinlobby/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllPlayersNotInLobby() {
		MineCraftPlayerService service = new MineCraftPlayerService();
		Collection<MineCraftPlayer> list = service.findAllPlayersNotInLobby();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		MineCraftPlayerService service = new MineCraftPlayerService();
		MineCraftPlayer domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "MineCraftPlayer removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("MineCraftPlayer não encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePlayer(@PathParam("uuid") String _uuid, String json ) {
		log("uuid recebido: " + _uuid );
		MineCraftPlayerService service = new MineCraftPlayerService();
		MineCraftPlayer _player = service.find( UUID.fromString(_uuid) );
		if( _player != null) {
			
			MineCraftPlayer player = (MineCraftPlayer)JSONParser.getInstance().toObject(json, MineCraftPlayer.class);
			service.merge(player);
			
			json = JSONParser.getInstance().toJSONString(player);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("MineCraftPlayer não encontrado: " + _uuid).build();
		}
	}
	
	
}
