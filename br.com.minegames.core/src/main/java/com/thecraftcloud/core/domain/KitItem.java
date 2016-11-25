package com.thecraftcloud.core.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class KitItem extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID kit_item_uuid;
	
	@OneToOne
	private Kit kit;
	
	@OneToOne
	private Item item;
	
	@Enumerated
	private ProtectionType protection;
	
	@Enumerated
	private SharpnessType sharpness;

	public UUID getKit_item_uuid() {
		return kit_item_uuid;
	}

	public void setKit_item_uuid(UUID kit_item_uuid) {
		this.kit_item_uuid = kit_item_uuid;
	}

	public Kit getKit() {
		return kit;
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ProtectionType getProtection() {
		return protection;
	}

	public void setProtection(ProtectionType protection) {
		this.protection = protection;
	}

	public SharpnessType getSharpness() {
		return sharpness;
	}

	public void setSharpness(SharpnessType sharpness) {
		this.sharpness = sharpness;
	}
	
	
}
