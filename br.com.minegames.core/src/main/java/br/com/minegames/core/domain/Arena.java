package br.com.minegames.core.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Arena extends TransferObject {

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
	
	@OneToOne
	private Schematic schematic;
	
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
	
}
