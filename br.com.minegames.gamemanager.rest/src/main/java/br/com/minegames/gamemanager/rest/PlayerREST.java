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

import org.apache.logging.log4j.LogManager;

import br.com.minegames.core.domain.GamePlayer;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.service.PlayerService;

@Path("/player")
public class PlayerREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		LogManager.getLogger(PlayerREST.class).info("json recebido: " + json );
		PlayerService service = new PlayerService();
		GamePlayer domain = (GamePlayer)JSONParser.getInstance().toObject(json, GamePlayer.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setGp_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o player com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		LogManager.getLogger(PlayerREST.class).info("uuid recebido: ");
		PlayerService service = new PlayerService();
		GamePlayer domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Player não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		PlayerService service = new PlayerService();
		Collection<GamePlayer> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		LogManager.getLogger(PlayerREST.class).info("uuid recebido: ");
		PlayerService service = new PlayerService();
		GamePlayer domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "Player removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Player não encontrado: " + _uuid).build();
		}
	}
	
	
}
