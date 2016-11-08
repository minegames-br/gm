package com.thecraftcloud.core.domain;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Area3D  extends TransferObject {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID area_uuid;
	
	@Column(unique=true)
	private String name;

	@OneToOne
	private Arena arena;

	@OneToOne(cascade=CascadeType.MERGE)
	private Local pointA;

	@OneToOne(cascade=CascadeType.MERGE)
	private Local pointB;
	
	private String type;

	public Area3D() {
		
	}
	
	public Area3D(Local pointA, Local pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
	}
	
	public Local getPointA() {
		return pointA;
	}
	public void setPointA(Local pointA) {
		this.pointA = pointA;
	}
	public Local getPointB() {
		return pointB;
	}
	public void setPointB(Local pointB) {
		this.pointB = pointB;
	}

	/*
	*/
	public UUID getArea_uuid() {
		return area_uuid;
	}

	public void setArea_uuid(UUID area_uuid) {
		this.area_uuid = area_uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
}
