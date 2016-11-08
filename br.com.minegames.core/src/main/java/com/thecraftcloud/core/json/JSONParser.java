package com.thecraftcloud.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {

	private static JSONParser me;
	private ObjectMapper mapper = new ObjectMapper();
	
	private JSONParser() {

	}
	
	public static JSONParser getInstance() {
		if( me == null) {
			me = new JSONParser();
		}
		return me;
	}

	public String toJSONString(Object to) {
		String json = null;
		
		try {
			json = mapper.writeValueAsString(to);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	public Object toObject(String json, Class _class) {
		Object to = null;
		try {
			to = mapper.readValue(json, _class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return to;
	}

}
