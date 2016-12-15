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

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.ArenaService;

@Path("/arena")
public class ArenaREST extends REST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		ArenaService service = new ArenaService();
		log("create arena: " + json);
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
	
	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArenaByName(@PathParam("name") String _name) {
		ArenaService service = new ArenaService();
		Arena domain = service.findArenaByName(_name);
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Arena não encontrado: " + _name).build();
		}
	}
	
	@POST
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("uuid") String _uuid, String json) {
		ArenaService service = new ArenaService();
		log("json: " + json);
		Arena domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			domain = (Arena)JSONParser.getInstance().toObject(json, Arena.class);
			service.update(domain);
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
        log("Download structure file for arena: " + uuid);

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
	
    @GET
    @Path("/{uuid}/world")
    @Produces("text/plain")
    public Response downloadWorldDirectory(@PathParam("uuid") String uuid) 
    {
        log("Download world ZIPed directory for arena: " + uuid);

		ArenaService service = new ArenaService();
		Arena domain = service.find( UUID.fromString(uuid) );
		if( domain != null) {
	        File file = new File( "/opt/mg/worlds/" + domain.getName()+ ".zip" );
	        ResponseBuilder response = Response.ok((Object) file);
	        response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
	        return response.build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Arena not found: " + uuid).build();
		}
        
    }	
}
