package br.com.minegames.gamemanager.rest;

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

import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.service.ArenaService;

@Path("/arena")
public class ArenaREST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		ArenaService service = new ArenaService();
		System.out.println("create arena: " + json);
		Arena domain = (Arena)JSONParser.getInstance().toObject(json, Arena.class);
		if(domain != null) {
			UUID uuid = service.save(domain);
			domain.setArena_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o Arena com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		ArenaService service = new ArenaService();
		Arena domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Arena não encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uuid") String _uuid, String json) {
		ArenaService service = new ArenaService();
		Arena domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			domain = (Arena)JSONParser.getInstance().toObject(json, Arena.class);
			service.merge(domain);
			domain = service.find( UUID.fromString(_uuid) );
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Arena não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		ArenaService service = new ArenaService();
		Collection<Arena> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		ArenaService service = new ArenaService();
		Arena domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "Arena removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Arena não encontrado: " + _uuid).build();
		}
	}
	
    @GET
    @Path("/{uuid}/schematic")
    @Produces("text/plain")
    public Response getFileInTextFormat(@PathParam("uuid") String uuid) 
    {
        System.out.println("Download structure file for arena: " + uuid);

		ArenaService service = new ArenaService();
		Arena domain = service.find( UUID.fromString(uuid) );
		if( domain != null) {
	        File file = new File( domain.getSchematic().getPath() + "/" + domain.getSchematic().getSchematic_uuid() + ".schematic" );
	        ResponseBuilder response = Response.ok((Object) file);
	        response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
	        return response.build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Arena not found: " + uuid).build();
		}
        
    }	
}
