package com.thecraftcloud.rest;

import java.io.File;
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
import javax.ws.rs.core.Response.ResponseBuilder;

import com.thecraftcloud.core.domain.AdminQueue;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.AdminService;
import com.thecraftcloud.service.ArenaService;

@Path("/admin")
public class AdminREST extends REST {
	
	@POST
	@Path("/sendtolobby/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendPlayerToLobby(@PathParam("name") String name) {
		log("send player to lobby name: " + name ); 
		AdminService aService = new AdminService();
		aService.teleportPlayerToLobby(name);
	    return Response.ok().build();
	}
	
	@POST
	@Path("/queue/markcomplete/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response markRequestCompleted(@PathParam("uuid") String uuid) {
		
		AdminService aService = new AdminService();
		aService.markRequestComplete(uuid);
	    return Response.ok().build();
	}
	
	@GET
	@Path("/queue/open/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllAdminRequest() {
		AdminService service = new AdminService();
		Collection<AdminQueue> list = service.findAllRequestsOpened();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
}
