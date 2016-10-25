package br.com.minegames.core.dto;

public class SearchGameWorldDTO {
	
	private String server_uuid;
	private String worldName;
	
	public String getServer_uuid() {
		return server_uuid;
	}
	public void setServer_uuid(String server_uuid) {
		this.server_uuid = server_uuid;
	}
	public String getWorldName() {
		return worldName;
	}
	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}
	
}
