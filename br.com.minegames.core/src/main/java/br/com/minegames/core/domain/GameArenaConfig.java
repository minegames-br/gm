package br.com.minegames.core.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GameArenaConfig extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID ga_config_uuid;
	
	@OneToOne
	private Game game;
	
	@OneToOne
	private Arena arena;
	
	@OneToMany
	private List<Config> simpleConfigList;
	
	@ElementCollection
	private Map<String, Config> configMap;

	public UUID getGa_config_uuid() {
		return ga_config_uuid;
	}

	public void setGa_config_uuid(UUID ga_config_uuid) {
		this.ga_config_uuid = ga_config_uuid;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public List<Config> getSimpleConfigList() {
		return simpleConfigList;
	}

	public void setSimpleConfigList(List<Config> simpleConfigList) {
		this.simpleConfigList = simpleConfigList;
	}

	public Map<String, Config> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<String, Config> configMap) {
		this.configMap = configMap;
	}
	
	
	
}
