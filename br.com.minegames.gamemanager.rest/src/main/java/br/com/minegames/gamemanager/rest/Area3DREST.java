package br.com.minegames.gamemanager.rest;

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

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.service.Area3DService;

@Path("/area")
public class Area3DREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		Area3DService service = new Area3DService();
		Area3D domain = (Area3D)JSONParser.getInstance().toObject(json, Area3D.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setArea_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
			System.out.println("json create area: " + json);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o Area3D com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		Area3DService service = new Area3DService();
		Area3D domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Area3D não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		Area3DService service = new Area3DService();
		Collection<Area3D> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		Area3DService service = new Area3DService();
		Area3D domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "Area3D removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Area3D não encontrado: " + _uuid).build();
		}
	}
	
	
}
