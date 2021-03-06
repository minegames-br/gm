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

import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerType;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.ItemService;
import com.thecraftcloud.service.LocalService;
import com.thecraftcloud.service.ServerService;

@Path("/server")
public class ServerREST extends REST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log("json recebido: " + json );
		ServerService service = new ServerService();
		ServerInstance domain = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setServer_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("N�o � possivel criar o servidor com as informa��es fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		ServerService service = new ServerService();
		ServerInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Servidor n�o encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uuid") String _uuid, String json) {
		log("uuid recebido: ");
		ServerService service = new ServerService();
		ServerInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			domain = (ServerInstance)JSONParser.getInstance().toObject(json, ServerInstance.class);
			if(domain.getLobby() != null && domain.getLobby().getLocal_uuid() == null) {
				LocalService lService = new LocalService();
				UUID uuid = lService.create(domain.getLobby());
				domain.getLobby().setLocal_uuid(uuid);
			}
			service.merge(domain);
			domain = service.find( UUID.fromString(_uuid) );
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Servidor n�o encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		ServerService service = new ServerService();
		Collection<ServerInstance> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/list/online")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllServerOnline() {
		ServerService service = new ServerService();
		Collection<ServerInstance> list = service.findAllOnline();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/game/list/online")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGameServerOnline() {
		ServerService service = new ServerService();
		Collection<ServerInstance> list = service.findAllOnlineByType(ServerType.GAME);
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		ServerService service = new ServerService();
		ServerInstance domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "Servidor removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Servidor n�o encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findItemByName(@PathParam("name") String name) {
		log("name: " + name);
		ServerService service = new ServerService();
		ServerInstance domain = service.findByName( name );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Server encontrado: " + name).build();
		}
	}
	
	@GET
	@Path("/search/byhost/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findItemByHostname(@PathParam("name") String name) {
		log("name: " + name);
		ServerService service = new ServerService();
		ServerInstance domain = service.findByHostname( name );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Server n�o encontrado: " + name).build();
		}
	}
	
	
}
