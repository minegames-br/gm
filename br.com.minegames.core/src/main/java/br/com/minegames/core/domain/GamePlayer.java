package br.com.minegames.core.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GamePlayer extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID gp_uuid;
	
	//Minecraft/Mojang UUID
	@Column(unique=true)
	private String player_uuid;
	
	@Column(unique=true)
	private String email;
	
	private String name;
	
	@Column(unique=true)
	private String nickName;
	public UUID getGp_uuid() {
		return gp_uuid;
	}
	public void setGp_uuid(UUID gp_uuid) {
		this.gp_uuid = gp_uuid;
	}
	public String getPlayer_uuid() {
		return player_uuid;
	}
	public void setPlayer_uuid(String player_uuid) {
		this.player_uuid = player_uuid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	

}
