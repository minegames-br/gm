package com.thecraftcloud.core.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Arena extends TransferObject implements Comparable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID arena_uuid;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	private List<Area3D> areas;
	@Column(unique=true)
	private String name;
	private String description;
	
	private Integer time = 12000;
	
	@OneToOne
	private Schematic schematic;
	
	@OneToOne
	private Area3D area;
	
	@Enumerated
	private FacingDirection facing;
	
	public UUID getArena_uuid() {
		return arena_uuid;
	}
	public void setArena_uuid(UUID arena_uuid) {
		this.arena_uuid = arena_uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Area3D> getAreas() {
		return areas;
	}
	public void setAreas(List<Area3D> areas) {
		this.areas = areas;
	}
	public Schematic getSchematic() {
		return schematic;
	}
	public void setSchematic(Schematic schematic) {
		this.schematic = schematic;
	}
	public Area3D getArea() {
		return area;
	}
	public void setArea(Area3D area) {
		this.area = area;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public FacingDirection getFacing() {
		return facing;
	}
	public void setFacing(FacingDirection facing) {
		this.facing = facing;
	}
	
	@Override
	public int compareTo(Object o) {
		Arena arena = (Arena)o;
		return arena.getArena_uuid().compareTo(this.getArena_uuid());
	}
	
	
}
