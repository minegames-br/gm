package com.thecraftcloud.core.domain;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Kit extends TransferObject {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID kit_uuid;
	
	@Column(unique=true)
	private String name;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	private List<Item> items;

	public UUID getKit_uuid() {
		return kit_uuid;
	}

	public void setKit_uuid(UUID kit_uuid) {
		this.kit_uuid = kit_uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
}
