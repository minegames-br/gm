package br.com.minegames.core.export;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

public class ExportBlock {

	private int x;
	private int y;
	private int z;
	
	private float yaw;
	private float pitch;
	
	private DyeColor color;
	private int typeId;
	private Material material;
	private String data;
	private SandstoneType sandStoneType;
	private BlockFace face;
	private String inverted;
	
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getData() {
		return data;
	}

	public void setData(String value) {
		this.data = value;
	}

	public DyeColor getColor() {
		return color;
	}

	public void setColor(DyeColor color) {
		this.color = color;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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

	public SandstoneType getSandstoneType() {
		return this.sandStoneType;
	}
	
	public void setSantstoneType(SandstoneType valueOf) {
		this.sandStoneType = valueOf;
	}

	public void setYaw(float value) {
		this.yaw = value;
	}
	
	public float getYaw() {
		return this.yaw;
	}

	public void setPitch(float value) {
		this.pitch  = value;
	}
	
	public float getPitch() {
		return this.pitch;
	}

	public SandstoneType getSandStoneType() {
		return sandStoneType;
	}

	public void setSandStoneType(SandstoneType sandStoneType) {
		this.sandStoneType = sandStoneType;
	}

	public BlockFace getFace() {
		return face;
	}

	public void setFace(BlockFace face) {
		this.face = face;
	}

	public void setInverted(String value) {
		this.inverted = value;
	}
	public String getInverted() {
		
		return inverted;
	}
	
	
}
