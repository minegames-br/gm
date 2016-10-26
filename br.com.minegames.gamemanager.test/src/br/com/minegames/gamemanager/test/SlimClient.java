package br.com.minegames.gamemanager.test;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

public class SlimClient {
	
	public static void main(String args[]) throws Exception {
		ClientRequest request = new ClientRequest("http://localhost:8080/gamemanager/webresources/schematic/upload");
		request.accept("application/json");
		
		MultipartFormDataOutput upload = new MultipartFormDataOutput();
		upload.addFormData("firstname", "teste.schematic", MediaType.TEXT_PLAIN_TYPE);
		upload.addFormData("lastname", "teste.schematic", MediaType.TEXT_PLAIN_TYPE);
		upload.addFormData("file", FileUtils.readFileToByteArray(new File("d:/minecraft/worlds/schematics/S2.schematic")), MediaType.APPLICATION_OCTET_STREAM_TYPE);
		request.body(MediaType.MULTIPART_FORM_DATA_TYPE, upload);
		ClientResponse<?> recording_response = request.post();
	}
}