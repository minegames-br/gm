package br.com.minegames.gamemanager.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameConfigScope;
import br.com.minegames.core.domain.GameConfigType;
import br.com.minegames.core.domain.GameConfigInstance;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.hologram.HologramUtil;
import br.com.minegames.core.util.title.TitleUtil;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.command.MineGamesCommand;
import br.com.minegames.gamemanager.plugin.listener.PlayerOnClick;

public class MineGamesPlugin extends JavaPlugin {

    private YamlConfiguration configFile;
	private File file = new File(getDataFolder(), "minegames.yml");
	
	private Area3D selection;
	private String server_uuid;
	private Arena arena;
	private Game game;
	private GameManagerDelegate delegate;
	
	private List<Arena> arenas;
	private List<Game> games;
	private GameConfig gameConfig;
	private String configValue;
	private List<GameConfig> configList;
	private Location setupLocation;
	private List<GameConfig> listConfigInts;
	private HashMap<String, GameArenaConfig> gameConfigArenaMap = new HashMap<String, GameArenaConfig>();
	private HashMap<String, GameConfigInstance> gameGameConfigMap = new HashMap<String, GameConfigInstance>();
	private int indexConfig = 0;
	private GameArenaConfig gac;
	private GameConfigInstance gcc;
	
	public List<Arena> getArenas() {
		return arenas;
	}

	public void setArenas(List<Arena> arenas) {
		this.arenas = arenas;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	@Override
	public void onEnable() {
		
		this.gameConfig = new GameConfig();
		this.gameConfig.setConfigScope(GameConfigScope.GLOBAL);
		this.gameConfig.setConfigType(GameConfigType.INT);
		this.gameConfig.setGroup("");
		this.gameConfig.setName("thelastarcher.countdown");
		this.gameConfig.setDisplayName("Countdown");
		
		this.setConfigValue("9");
		
		getCommand("mg").setExecutor(new MineGamesCommand(this));
	    if(!file.exists()){
	        file.getParentFile().mkdirs();
	        try {
				file.createNewFile();
				this.configFile = YamlConfiguration.loadConfiguration(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    } else {
			this.configFile = YamlConfiguration.loadConfiguration(file);
	    }

	    this.delegate = GameManagerDelegate.getInstance();
	    //this.game = delegate.findGame("57b7b3df-9d18-4966-898f-f4ad8ee28a92");
	    registerListeners();
	}
	
	public YamlConfiguration getConfigFile() {
		try {
			this.configFile.load(this.file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.configFile;
	}

	public void setConfig(String path, String value) {
		configFile.set(path, value);
		try {
			configFile.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerOnClick(this), this);
	}

	public Area3D getSelection() {
		return selection;
	}

	public void setSelection(Area3D selection) {
		this.selection = selection;
	}
	
	public void setSelectionPointA(Location l) {
		Local local = new Local(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		if(selection == null) {
			this.selection = new Area3D();
		}
		selection.setPointA(local);
	}

	public void setSelectionPointB(Location l) {
		Local local = new Local(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		if(selection == null) {
			this.selection = new Area3D();
		}
		selection.setPointB(local);
	}

	public String getServer_uuid() {
		return this.server_uuid;
	}

	public boolean isServerRegistered() {
		YamlConfiguration config = this.getConfigFile();
		String value = config.getString("minegames.server.uuid");
		Bukkit.getLogger().info("minegames.server.uuid: " + value);
		if(value != null && !value.trim().equals("")) {
			return false;
		}
		
		return false;
	}

	public Arena getArena() {
		return this.arena;
	}
	
	public Game getGame() {
		return this.game;
	}

	public List<Game> getGames() {
		return this.games;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public String getConfigName() {
		return this.gameConfig.getName();
	}
	
	public GameConfig getGameConfig() {
		return this.gameConfig;
	}
	
	public void setGameConfig(GameConfig value) {
		this.gameConfig = value;
	}

	public String getConfigValue() {
		return this.configValue;
	}
	public void setConfigValue(String value) {
		this.configValue = value;
	}
	
	public void updateConfigHologram(Player player) {
		HologramUtil.showPlayer( player, new String[]{this.getGameConfig().getName(), configValue.toString()}, new Location(player.getWorld(), -766, 5, 402) );
	}

	public void setConfigList(List<GameConfig> list) {
		this.configList = list;
	}
	
	public List<GameConfig> getConfigList() {
		return this.configList;
	}

	public void saveGameConfig() {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		final MineGamesPlugin plugin = this;
		final GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
        		for(GameConfigInstance gcc: plugin.getGameGameConfigMap().values() ) {
        			delegate.addGameConfigInstance(gcc);
        		}
        		
        		for(GameArenaConfig gac: plugin.getGameConfigArenaMap().values()) {
        			//delegate.addGameAreanaConfigInstance(gac);
        		}
            }
        }, 20L);
	}

	public void setupGameArenaConfig(Player player) {
		this.setupLocation = new Location(player.getWorld(), -764, 5, 402);
		player.teleport(this.setupLocation);
		
		//setup int values 
		List<GameConfig> listInts = new ArrayList<GameConfig>();
		for(GameConfig gc: this.configList) {
			if(gc.getConfigType() == GameConfigType.INT) {
				listInts.add(gc);
			}
		}
		
		this.setListConfigInts(listInts);
		
		this.gameConfig = listInts.get(0);
		
		TitleUtil.sendTitle(player, 10, 20, 10, "Time to setup the game", "Click on levers to change value and switch configs. Open the door to save your configs.");
		
		updateConfigHologram(player);
	}
	
	public void updateConfigValue() {
		this.gac = getGameArenaConfigByName(this.gameConfig.getName());
		if(this.gac == null) {
			this.gac = new GameArenaConfig();
			this.gameConfigArenaMap.put(this.gameConfig.getName(), this.gac);
			this.configValue="0";
		} else {
			if(this.gac.getIntValue()==null) {
				this.configValue="0";
			}else{
				this.configValue=this.gac.getIntValue().toString();
			}
		}
	}
	
	public void nextConfig( Player player ) {
		updateConfig();
		this.indexConfig ++;
		if(this.indexConfig > listConfigInts.size()) {
			indexConfig = 0;
		}
		this.gameConfig = listConfigInts.get(indexConfig);
		updateConfigValue();
		updateConfigHologram(player);
	}
	
	
	public void previousConfig( Player player ) {
		updateConfig();
		this.indexConfig --;
		if(this.indexConfig < 0 ) {
			indexConfig = listConfigInts.size()-1;
		}
		this.gameConfig = listConfigInts.get(indexConfig);
		updateConfigValue();
		updateConfigHologram(player);
	}
	
	public void updateConfig( ) {
		Object configObject = this.getConfigValue();
		
		if(configObject instanceof Integer) {
			String key = this.getGameConfig().getName();
			if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
				this.gac.setArena(this.arena);
				this.gac.setGameConfig(this.gameConfig);
				if(this.gameConfig.getConfigType() == GameConfigType.INT) {
					this.gac.setIntValue(Integer.parseInt(this.configValue));
				}
			} else {
				this.gcc.setGameConfig(this.gameConfig);
				if(this.gameConfig.getConfigType() == GameConfigType.INT) {
					this.gcc.setIntValue(Integer.parseInt(this.configValue));
				}
			}
			
		}
	}
	
	public GameArenaConfig getGameArenaConfigByName(String name) {
		GameArenaConfig gac = null;
		
		if(gameConfigArenaMap.get(name) != null) {
			gac = gameConfigArenaMap.get(name);
		}
		
		return gac;
	}

	public void setListConfigInts(List<GameConfig> listInts) {
		this.listConfigInts = listInts;
	}
	
	public List<GameConfig> getListConfigInts() {
		return this.listConfigInts;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public GameManagerDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(GameManagerDelegate delegate) {
		this.delegate = delegate;
	}

	public Location getSetupLocation() {
		return setupLocation;
	}

	public void setSetupLocation(Location setupLocation) {
		this.setupLocation = setupLocation;
	}

	public HashMap<String, GameArenaConfig> getGameConfigArenaMap() {
		return gameConfigArenaMap;
	}

	public void setGameConfigArenaMap(HashMap<String, GameArenaConfig> gameConfigArenaMap) {
		this.gameConfigArenaMap = gameConfigArenaMap;
	}

	public void setConfigFile(YamlConfiguration configFile) {
		this.configFile = configFile;
	}

	public void setServer_uuid(String server_uuid) {
		this.server_uuid = server_uuid;
	}

	public HashMap<String, GameConfigInstance> getGameGameConfigMap() {
		return gameGameConfigMap;
	}

	public void setGameGameConfigMap(HashMap<String, GameConfigInstance> gameGameConfigMap) {
		this.gameGameConfigMap = gameGameConfigMap;
	}
	
}
