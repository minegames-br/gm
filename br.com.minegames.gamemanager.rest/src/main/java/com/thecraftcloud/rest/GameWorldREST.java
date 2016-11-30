package com.thecraftcloud.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.GameConfigService;
import com.thecraftcloud.service.GameWorldService;
import com.thecraftcloud.service.SchematicService;

@Path("/world")
public class GameWorldREST {
	
	@Path("/{uuid}/upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@MultipartForm MyEntity entity, @PathParam("uuid") String _uuid) throws IOException {

		GameWorldService service = new GameWorldService();
		GameWorld domain = service.find( UUID.fromString(_uuid) );
		String path = GameWorldService.WORLD_PATH + "/" + domain.getName() + ".zip";
		
	    try (FileOutputStream fos = new FileOutputStream( path )) {
	        byte[] filebytes = entity.getFile();
	        fos.write(filebytes);
	    }
	    
	    domain.setPath(path);
	    service.merge(domain);

	    return Response.ok("File uploaded successfully").build();
	}
	
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
	
    @GET
    @Path("/{uuid}/download")
    @Produces("text/plain")
    public Response downloadWorld(@PathParam("uuid") String uuid) {
        System.out.println("Download world ZIPed directory: " + uuid);

        GameWorldService service = new GameWorldService();
        GameWorld domain = service.find( UUID.fromString(uuid) );
		if( domain != null) {
	        File file = new File( "/opt/mg/worlds/" + domain.getName() + ".zip" );
	        ResponseBuilder response = Response.ok((Object) file);
	        response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
	        return response.build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameWorld not found: " + uuid).build();
		}
    }

	@GET
	@Path("/search/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGameWorldByName(@PathParam("name") String name) {
		System.out.println("name: " + name);
		GameWorldService service = new GameWorldService();
		GameWorld domain = service.findByName( name );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("GameWorld não encontrado: " + name).build();
		}
	}
	
}
