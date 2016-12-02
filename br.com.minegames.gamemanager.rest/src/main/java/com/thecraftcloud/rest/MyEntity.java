package com.thecraftcloud.rest;

import javax.ws.rs.FormParam;

public class MyEntity extends REST {

    @FormParam("file")
    private byte[] file;

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

    // Getter and Setters
}