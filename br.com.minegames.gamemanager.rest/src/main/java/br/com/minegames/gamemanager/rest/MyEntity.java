package br.com.minegames.gamemanager.rest;

import javax.ws.rs.FormParam;

public class MyEntity {

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