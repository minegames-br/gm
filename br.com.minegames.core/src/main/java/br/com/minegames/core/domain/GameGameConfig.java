package br.com.minegames.core.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class GameGameConfig extends TransferObject {

	@Id
	@OneToOne
	private Game game;
	
	@Id
	@OneToOne
	private GameConfig gameConfig;
	
	@OneToOne
	private Local local;
	
	@OneToOne
	private Area3D area;
	
	private int intValue;
	
	private GameConfigType type;
	
	@OneToMany
	private List<Local> localList;
	
	@OneToMany
	private List<Area3D> areaList;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public GameConfig getGameConfig() {
		return gameConfig;
	}

	public void setGameConfig(GameConfig gameConfig) {
		this.gameConfig = gameConfig;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Area3D getArea() {
		return area;
	}

	public void setArea(Area3D area) {
		this.area = area;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public GameConfigType getType() {
		return type;
	}

	public void setType(GameConfigType type) {
		this.type = type;
	}

	public List<Local> getLocalList() {
		return localList;
	}

	public void setLocalList(List<Local> localList) {
		this.localList = localList;
	}

	public List<Area3D> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Area3D> areaList) {
		this.areaList = areaList;
	}

	
	
}
