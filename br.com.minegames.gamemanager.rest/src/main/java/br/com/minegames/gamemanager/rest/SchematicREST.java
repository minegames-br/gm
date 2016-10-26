package br.com.minegames.gamemanager.rest;

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

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import br.com.minegames.core.domain.Schematic;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.service.SchematicService;

@Path("/schematic")
public class SchematicREST {

	@Path("/upload/{uuid}")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@MultipartForm MyEntity entity, @PathParam("uuid") String _uuid) throws IOException {

	    try (FileOutputStream fos = new FileOutputStream( SchematicService.SCHEMATIC_PATH + "/" + _uuid + ".schematic" )) {
	        byte[] filebytes = entity.getFile();
	        fos.write(filebytes);
	    }

	    return Response.ok("File uploaded successfully").build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		Logger.getLogger(PlayerREST.class).info("json recebido: " + json );
		SchematicService service = new SchematicService();
		Schematic domain = (Schematic)JSONParser.getInstance().toObject(json, Schematic.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setSchematic_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o Schematic com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		Logger.getLogger(PlayerREST.class).info("uuid recebido: ");
		SchematicService service = new SchematicService();
		Schematic domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Schematic não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		SchematicService service = new SchematicService();
		Collection<Schematic> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		Logger.getLogger(PlayerREST.class).info("uuid recebido: ");
		SchematicService service = new SchematicService();
		Schematic domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "Schematic removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Schematic não encontrado: " + _uuid).build();
		}
	}
	
	
}