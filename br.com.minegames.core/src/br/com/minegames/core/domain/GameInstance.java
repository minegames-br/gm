package br.com.minegames.core.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.bukkit.Bukkit;
import org.hibernate.annotations.GenericGenerator;

import br.com.minegames.core.json.JSONParser;

@Entity
public class GameInstance extends TransferObject {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID gi_uuid;
	
	@OneToOne
	private Game game;
	
	@OneToOne 
	private ServerInstance server;

	@OneToOne
	private GameWorld world;
	
	@OneToOne
	private Arena arena;
	
	@OneToMany
	private List<Config> configs;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endTime;
	
	@Enumerated(EnumType.STRING)
	private GameState status; 
	
	public void setGi_uuid(UUID gi_uuid) {
		this.gi_uuid = gi_uuid;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public ServerInstance getServer() {
		return server;
	}
	public void setServer(ServerInstance server) {
		this.server = server;
	}
	public Calendar getStartTime() {
		return startTime;
	}
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	public Calendar getEndTime() {
		return endTime;
	}
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}
	public GameState getStatus() {
		return status;
	}
	public void setStatus(GameState status) {
		this.status = status;
	}
	public UUID getGi_uuid() {
		return gi_uuid;
	}
	public GameWorld getWorld() {
		return world;
	}
	public void setWorld(GameWorld world) {
		this.world = world;
	}
	public Arena getArena() {
		return arena;
	}
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	public List<Config> getConfigs() {
		return configs;
	}
	public void setConfigs(List<Config> configs) {
		this.configs = configs;
	}
	
	public int getConfigIntValue(String configName) {
		Config config = null;
		for(Config c: configs) {
			if(c.getName().equalsIgnoreCase(configName)){
				config = c;
				break;
			}
		}
		
		if(config != null) {
			return config.getIntValue();
		} else {
			return 0;
		}
		
	}

	public Area3D getArea(String name) {
		Area3D area = null;
		for(Area3D a : this.arena.getAreas() ) {
			if(a.getName().equalsIgnoreCase(name)){
				area = a;
				break;
			}
		}
		
		return area;
		
	}

	public List<Area3D> getAreaListByType(String areaType) {

		Bukkit.getLogger().info("area type: " + areaType);
		List<Area3D> areas = new ArrayList<Area3D>();
		
		for(Area3D area: this.arena.getAreas()) {
			Bukkit.getLogger().info("area type: " + areaType);
			if(area.getType().equalsIgnoreCase(areaType)) {
				areas.add(area);
			}
		}
		
		return areas;
	}
	
	public List<Config> getConfigListByType(String configType) {

		List<Config> configs = new ArrayList<Config>();
		
		for(Config config: configs) {
			if(config.getType().equalsIgnoreCase(configType)) {
				configs.add(config);
			}
		}
		
		return configs;
	}
	
	
	public Config getConfigByName(String name) {
		Config config = null;
		
		for(Config c: this.configs) {
			if(c.getName().equalsIgnoreCase(name)) {
				config = c;
				break;
			}
		}
		
		return config;
	}
	
	public Local getLocalByConfig(String lobbyLocation) {
		Local local = null;
		Config config = getConfigByName(lobbyLocation);
		local = (Local)JSONParser.getInstance().toObject(config.getValue(), Local.class);
		return local;
	}
}
