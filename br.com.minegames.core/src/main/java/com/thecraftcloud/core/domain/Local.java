package com.thecraftcloud.core.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Local  extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID local_uuid;
	
	private int x;
	private int y;
	private int z;
	
	private float yaw;
	private float pitch;
	
	@Enumerated
	private FacingDirection facingDirection;
	
	private String name;
	
	public Local() {
		super();
	}
	
	public Local(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Local(int x, int y, int z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;      
		this.pitch = pitch;
	}
	
	public UUID getLocal_uuid() {
		return local_uuid;
	}

	public void setLocal_uuid(UUID local_uuid) {
		this.local_uuid = local_uuid;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "" + x + "," + y + "," + z;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public FacingDirection getFacingDirection() {
		return facingDirection;
	}

	public void setFacingDirection(FacingDirection facingDirection) {
		this.facingDirection = facingDirection;
	}

	
	
}
