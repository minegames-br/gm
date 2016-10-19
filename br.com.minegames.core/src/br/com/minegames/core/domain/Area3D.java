package br.com.minegames.core.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.bukkit.Location;
import org.bukkit.World;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Area3D  extends TransferObject {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID area_uuid;
	
	private String name;
	
	@OneToOne
	private Local pointA;

	@OneToOne
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

	public int getXSize() {
		return this.pointB.getX()-this.pointA.getX();
	}
	
	public int getYSize() {
		return this.pointB.getY()-this.pointA.getY();
	}
	
	public int getZSize() {
		return this.pointB.getZ()-this.pointA.getZ();
	}
	
	public Location getMiddle(World world) {
        int middleX = (this.pointA.getX() + this.pointB.getX()) / 2;
        int middleZ = (this.pointA.getZ() + this.pointB.getZ()) / 2;
        Location l = new Location(world, middleX, pointA.getY(), middleZ);
        return l;
    }

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
	
}
