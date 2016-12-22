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
import com.thecraftcloud.core.domain.Kit;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.service.ItemService;
import com.thecraftcloud.service.KitService;

@Path("/kit")
public class KitREST extends REST {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String json) {
		log("json recebido: " + json );
		KitService service = new KitService();
		Kit domain = (Kit)JSONParser.getInstance().toObject(json, Kit.class);
		if(domain != null) {
			UUID uuid = service.create(domain);
			domain.setKit_uuid(uuid);
			json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.CONFLICT).entity("Não é possivel criar o Kit com as informações fornecidas").build();
		}
	}
	
	@GET
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		KitService service = new KitService();
		Kit domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			String json = JSONParser.getInstance().toJSONString(domain);
		    return Response.ok( json , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Kit não encontrado: " + _uuid).build();
		}
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGames() {
		KitService service = new KitService();
		Collection<Kit> list = service.findAll();
		String json = JSONParser.getInstance().toJSONString(list);
		return Response.ok(json, MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Path("/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("uuid") String _uuid) {
		log("uuid recebido: ");
		KitService service = new KitService();
		Kit domain = service.find( UUID.fromString(_uuid) );
		if( domain != null) {
			service.delete(domain);
		    return Response.ok( "Kit removido com sucesso" , MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Kit não encontrado: " + _uuid).build();
		}
	}
	
	@POST
	@Path("/{kit_uuid}/additem/{item_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("kit_uuid") String kit_uuid, @PathParam("item_uuid") String item_uuid ) {
		KitService service = new KitService();
		ItemService iService = new ItemService();
		Kit kit = service.find( UUID.fromString(kit_uuid) );
		Item item = iService.find(UUID.fromString(item_uuid));
		if( kit != null && item != null) {
			kit = service.addItem(kit, item);
		    return Response.ok( MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Kit ou Item não encontrado").build();
		}
	}
	
	
}
