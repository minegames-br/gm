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

import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameQueueService;

@Path("/gamequeue")
public class GameQueueREST  extends REST {
	
	@POST
	@Path("/lock")
	@Produces(MediaType.APPLICATION_JSON)
	public Response lockQueue(String json) {
		log("lockQueue");
		System.out.println("lockQueue");
		GameQueueService service = new GameQueueService();
		log("lockQueue1");
		service.bulkStatusUpdate(GameQueueStatus.WAITING, GameQueueStatus.PROCESSING);
		log("lockQueue2");
	    return Response.ok("{teste:name}", MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log("json recebido: " + json );
		GameQueueService service = new GameQueueService();
		GameQueue domain = (GameQueue)JSONParser.getInstance().toObject(json, GameQueue.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setGame_queue_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o GameQueue com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		log("get");
		log("uuid recebido: ");
		GameQueueService service = new GameQueueService();
		GameQueue domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameQueue não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGameQueue() {
		log("gamequeue list");
		GameQueueService service = new GameQueueService();
		Collection<GameQueue> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/list/game/{game}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameQueueByGame(@PathParam("game") String game) {
		GameQueueService service = new GameQueueService();
		Collection<GameQueue> list = service.findAllGameQueueByGame(game);
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/list/status/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameQueueByStatus(@PathParam("status") String status) {
		GameQueueService service = new GameQueueService();
		Collection<GameQueue> list = service.findAllGameQueueByStatus(status);
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		log("GameQueueREST http delete - uuid recebido: " + _uuid);
		GameQueueService service = new GameQueueService();
		try {
			log("GameQueueREST http delete - service.delete " + _uuid );
			service.delete(_uuid);
			log("GameQueueREST http delete - build response ok" );
			return Response.ok().build();
		} catch (Exception e) {
			log("GameQueueREST http delete - build response error: " + e.getMessage() );
			e.printStackTrace();
			return Response.status(Response.Status.CONFLICT).entity("GameQueue nao pode ser apagado " + e.getMessage()).build();
		}
	}
	
	@POST
	@Path("/{uuid}/complete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response completeGameQueueRequest(@PathParam("uuid") String _uuid) {
		log("GameQueueREST http post - uuid recebido: " + _uuid);
		GameQueueService service = new GameQueueService();
		try {
			log("GameQueueREST http post - service.completeGameQueueRequest " + _uuid );
			GameQueue gq = service.completeGameQueueRequest(_uuid);
			String json = JSONParser.getInstance().toJSONString(gq);
			log("GameQueueREST http post - build response ok" );
			return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			log("GameQueueREST http port - build response error: " + e.getMessage() );
			e.printStackTrace();
			return Response.status(Response.Status.CONFLICT).entity("GameQueue nao pode ser completado " + e.getMessage()).build();
		}
	}
	
	
}
