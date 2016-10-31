package br.com.minegames.core.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class GameConfigInstance extends TransferObject {

	@Id
	@OneToOne(fetch = FetchType.EAGER)
	private GameConfig gameConfig;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Local local;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Area3D area;
	
	private int intValue;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	private List<Local> localList;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	private List<Area3D> areaList;

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

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		
		if(object instanceof GameConfigInstance) {
			GameConfigInstance gci = (GameConfigInstance)object;
			gci.gameConfig.equals(this.gameConfig);
		}
		
		return result;
	}
	
}
