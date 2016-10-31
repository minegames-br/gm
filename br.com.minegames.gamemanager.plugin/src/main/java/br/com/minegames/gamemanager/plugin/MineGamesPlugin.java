package br.com.minegames.gamemanager.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Stairs;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameConfigInstance;
import br.com.minegames.core.domain.GameConfigScope;
import br.com.minegames.core.domain.GameConfigType;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.hologram.HologramUtil;
import br.com.minegames.core.util.LocationUtil;
import br.com.minegames.core.util.title.TitleUtil;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.command.MineGamesCommand;
import br.com.minegames.gamemanager.plugin.listener.BlockBreakListener;
import br.com.minegames.gamemanager.plugin.listener.PlayerOnClick;
import br.com.minegames.gamemanager.plugin.task.ArenaSetupTask;

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
	private List<GameConfig> configList;
	private Location setupLocation;
	private List<GameConfig> listConfigInts;
	private HashMap<String, GameArenaConfig> gameConfigArenaMap = new HashMap<String, GameArenaConfig>();
	private HashMap<String, GameConfigInstance> gameGameConfigMap = new HashMap<String, GameConfigInstance>();

	private Player player;
	private World world;
	
	private Boolean setupArena = false;
	
	private int indexConfig = 0;
	private Object configValue;

	private int indexArenaConfig = 0;
	private Object configArenaValue;

	private GameArenaConfig gac;
	private GameConfigInstance gci;

	private ArenaSetupTask arenaSetupTask;
	private int arenaSetupTaskThreadID;
	
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
		Bukkit.getLogger().info("MineGamesPlugin - onEnable");
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
		pm.registerEvents(new BlockBreakListener(this), this);
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

	public Object getConfigValue() {
		return this.configValue;
	}
	public void setConfigValue(Object value) {
		this.configValue = value;
	}
	
	public void updateConfigHologram(Player player) {
		HologramUtil.showPlayer( player, new String[]{this.getGameConfig().getDisplayName(), configValue.toString()}, new Location(player.getWorld(), -761, 4, 402) );
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
		scheduler.runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
            	for(final GameConfigInstance gcc: plugin.getGameGameConfigMap().values() ) {
	            	delegate.addGameConfigInstance(gcc);
	            }
        		
	            for(final GameArenaConfig gac: plugin.getGameConfigArenaMap().values()) {
	            	delegate.createGameArenaConfig(gac);
	            }
            }
		});
	}

	public void setupGameArenaConfig(Player player) {
		this.setupLocation = new Location(player.getWorld(), -764, 5, 402);
		this.player = player;
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
		if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
			this.gac = getGameArenaConfigByName(this.gameConfig.getName());
			this.gci = null;
		} else {
			this.gac = null;
			this.gci = getGameConfigInstanceByName(this.gameConfig.getName());
		}
		updateConfigValue();
		
		TitleUtil.sendTitle(player, 10, 20, 10, "Time to setup the game", "Click on levers to change value and switch configs. Open the door to save your configs.");
		
		updateConfigHologram(player);
	}
	
	public void updateConfigValue() {
		
		if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
			this.gac = getGameArenaConfigByName(this.gameConfig.getName());
			if(this.gac == null) {
				this.gac = new GameArenaConfig();
				this.gac.setGameConfig(this.gameConfig);
				this.gameConfigArenaMap.put(this.gameConfig.getName(), this.gac);
				if(this.gameConfig.getConfigType() == GameConfigType.INT) {
					this.configValue= new Integer(0);
				} else {
					this.configValue = null;
				}
			} else {
				if(this.gac.getIntValue()==null) {
					this.configValue= new Integer(0);
				}else{
					this.configValue=this.gac.getIntValue();
				}
			}
		} else {
			this.gci = getGameConfigInstanceByName(this.gameConfig.getName());
			if(this.gci == null) {
				this.gci = new GameConfigInstance();
				this.setGameConfig(this.gameConfig);
				this.gameGameConfigMap.put(this.gameConfig.getName(), this.gci);
				if(this.gameConfig.getConfigType() == GameConfigType.INT) {
					this.configValue= new Integer(0);
				} else {
					this.configValue = null;
				}
			} else {
				if(this.gci.getIntValue() == 0) {
					this.configValue=new Integer(0);
				}else{
					if(this.gameConfig.getConfigType() == GameConfigType.INT) {
						this.configValue=this.gci.getIntValue();
					} 
				}
			}
		}
	}
	
	public void nextConfig( Player player ) {
		updateConfig();
		this.indexConfig ++;
		if(this.indexConfig == listConfigInts.size()) {
			indexConfig = 0;
		}
		this.gameConfig = listConfigInts.get(indexConfig);
		if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
			this.gac = getGameArenaConfigByName(this.gameConfig.getName());
			this.gci = null;
		} else {
			this.gac = null;
			this.gci = getGameConfigInstanceByName(this.gameConfig.getName());
		}
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
		if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
			this.gac = getGameArenaConfigByName(this.gameConfig.getName());
			this.gci = null;
		} else {
			this.gac = null;
			this.gci = getGameConfigInstanceByName(this.gameConfig.getName());
		}
		updateConfigValue();
		updateConfigHologram(player);
	}
	
	public void nextArenaConfig( Player player ) {
		if(this.gameConfig.getConfigType() == GameConfigType.LOCAL) {
			if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
				this.gac.setLocalValue((Local)this.configArenaValue);
			}else{
				this.gci.setLocal((Local)this.configArenaValue);
			}
		}if(this.gameConfig.getConfigType() == GameConfigType.AREA3D) {
			if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
				this.gac.setAreaValue((Area3D)this.configArenaValue);
			} else {
				this.gci.setArea((Area3D)this.configArenaValue);
			}
		}
		
		Bukkit.getLogger().info("nextArenaConfig");
		this.indexArenaConfig ++;
		if(this.indexArenaConfig == configList.size()) {
			Bukkit.getLogger().info("this.indexArenaConfig == configList.size()");
			completeArenaSetupTask();
			return;
		}
		
		boolean hasNext = false;
		for(int i = indexArenaConfig; i < configList.size(); i++) {
			this.gameConfig = configList.get(i);
			if(this.gameConfig.getConfigType() == GameConfigType.LOCAL || 
					this.gameConfig.getConfigType() == GameConfigType.AREA3D) {
				indexArenaConfig = i;
				hasNext = true;
				break;
			}
		}
		
		if(!hasNext) {
			Bukkit.getLogger().info("hasNext == false");
			return;
		}
		
		if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
			this.gac = getGameArenaConfigByName(this.gameConfig.getName());
			this.gci = null;
		} else {
			this.gac = null;
			this.gci = getGameConfigInstanceByName(this.gameConfig.getName());
		}
		updateArenaConfigValue();
	}
	
	public void updateArenaConfigValue() {
		
		Bukkit.getLogger().info("updateArenaConfigValue");
		
		if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
			this.gac = getGameArenaConfigByName(this.gameConfig.getName());
			if(this.gac == null) {
				this.gac = new GameArenaConfig();
				this.gac.setGameConfig(this.gameConfig);
				this.gac.setArena(this.arena);
				this.gameConfigArenaMap.put(this.gameConfig.getName(), this.gac);
				this.configArenaValue = null;
			} else {
				if(this.gameConfig.getConfigType() == GameConfigType.LOCAL) {
					this.configArenaValue = this.gac.getLocalValue();
				}if(this.gameConfig.getConfigType() == GameConfigType.AREA3D) {
					this.configArenaValue = this.gac.getAreaValue();
				}
			}
		} else {
			this.gci = getGameConfigInstanceByName(this.gameConfig.getName());
			if(this.gci == null) {
				this.gci = new GameConfigInstance();
				this.gci.setGameConfig(gameConfig);
				this.gameGameConfigMap.put(this.gameConfig.getName(), this.gci);
				this.configArenaValue = null;
			} else {
				if(this.gameConfig.getConfigType() == GameConfigType.LOCAL) {
					this.configArenaValue = this.gci.getLocal();
				}if(this.gameConfig.getConfigType() == GameConfigType.AREA3D) {
					this.configArenaValue = this.gci.getArea();
				}
			}
		}
		this.selection = new Area3D();
		this.configArenaValue = null;
		TitleUtil.sendTitle(player, 20, 40, 20, "Setup " + this.gameConfig.getConfigType(), "Config: " + this.gameConfig.getDisplayName() );
	}
	
	public void updateConfig( ) {
		Object configObject = this.getConfigValue();
		
		if(configObject instanceof Integer) {
			Bukkit.getConsoleSender().sendMessage("&6updateConfig: "  + this.gameConfig.getConfigScope() + " " + this.gameConfig.getName() + ": " + configObject.toString());
			String key = this.getGameConfig().getName();
			if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
				this.gac.setArena(this.arena);
				this.gac.setGameConfig(this.gameConfig);
				if(this.gameConfig.getConfigType() == GameConfigType.INT) {
					this.gac.setIntValue(Integer.parseInt(this.configValue.toString()));
					gameConfigArenaMap.put(gameConfig.getName(), gac);
				}
			} else {
				this.gci.setGameConfig(this.gameConfig);
				if(this.gameConfig.getConfigType() == GameConfigType.INT) {
					this.gci.setIntValue(Integer.parseInt(this.configValue.toString()));
					gameGameConfigMap.put(this.getConfigName(), gci);
				}
			}
		}else{
			Bukkit.getConsoleSender().sendMessage("&6Nao era pra entrar aqui");
		}
	}
	
	public GameArenaConfig getGameArenaConfigByName(String name) {
		GameArenaConfig gac = null;
		
		if(gameConfigArenaMap.get(name) != null) {
			gac = gameConfigArenaMap.get(name);
		}
		
		return gac;
	}

	public GameConfigInstance getGameConfigInstanceByName(String name) {
		GameConfigInstance gci = null;
		
		if(gameGameConfigMap.get(name) != null) {
			gci = gameGameConfigMap.get(name);
		}
		
		return gci;
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

	public void startArenaSetupTask( ) {
		Bukkit.getLogger().info("startArenaSetupTask");
		BukkitScheduler scheduler = Bukkit.getScheduler();
		this.arenaSetupTask = new ArenaSetupTask(this);
		this.arenaSetupTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.arenaSetupTask, 1L, 20L);
		player.getInventory().setItemInMainHand(new ItemStack(Material.IRON_AXE));
		Location loc = LocationUtil.getMiddle(player.getWorld(), this.arena.getArea());
		player.teleport(loc);
		this.setupArena = true;
		this.selection = new Area3D();
		this.configArenaValue = null;
		for(int i = 0; i < configList.size(); i++) {
			this.gameConfig = configList.get(i);
			if(this.gameConfig.getConfigType() == GameConfigType.LOCAL || 
					this.gameConfig.getConfigType() == GameConfigType.AREA3D) {
				indexArenaConfig = i;
				break;
			}
		}
		this.updateArenaConfigValue();
	}
	
	public void completeArenaSetupTask() {
		Bukkit.getLogger().info("completeArenaSetupTask");
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.cancelTask( this.arenaSetupTaskThreadID );
		this.setupArena = false;
		saveGameConfig();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getIndexConfig() {
		return indexConfig;
	}

	public void setIndexConfig(int indexConfig) {
		this.indexConfig = indexConfig;
	}

	public int getIndexArenaConfig() {
		return indexArenaConfig;
	}

	public void setIndexArenaConfig(int indexArenaConfig) {
		this.indexArenaConfig = indexArenaConfig;
	}

	public Object getConfigArenaValue() {
		return configArenaValue;
	}

	public void setConfigArenaValue(Object configArenaValue) {
		this.configArenaValue = configArenaValue;
	}

	public GameArenaConfig getGac() {
		return gac;
	}

	public void setGac(GameArenaConfig gac) {
		this.gac = gac;
	}

	public GameConfigInstance getGci() {
		return gci;
	}

	public void setGci(GameConfigInstance gci) {
		this.gci = gci;
	}

	public ArenaSetupTask getArenaSetupTask() {
		return arenaSetupTask;
	}

	public void setArenaSetupTask(ArenaSetupTask arenaSetupTask) {
		this.arenaSetupTask = arenaSetupTask;
	}

	public int getArenaSetupTaskThreadID() {
		return arenaSetupTaskThreadID;
	}

	public void setArenaSetupTaskThreadID(int arenaSetupTaskThreadID) {
		this.arenaSetupTaskThreadID = arenaSetupTaskThreadID;
	}

	public Boolean getSetupArena() {
		return setupArena;
	}

	public void setSetupArena(Boolean setupArena) {
		this.setupArena = setupArena;
	}

	public void onPlayerClickSetupArena(PlayerInteractEvent event) {
		if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.IRON_AXE) {
			return;
		}
		
		if(event.getClickedBlock() == null) {
			return;
		}
		
		Bukkit.getLogger().info("onPlayerClickSetupArena");
		
		Location location = event.getClickedBlock().getLocation();
		Local l = new Local(location.getBlockX(), location.getBlockY(), location.getBlockZ());

		if(event.getAction() == Action.LEFT_CLICK_BLOCK ) {
			if(this.gameConfig.getConfigType() == GameConfigType.LOCAL) {
				this.configArenaValue = l;
				player.sendMessage("set Local: " + this.gameConfig.getName() );
				nextArenaConfig(player);
				Bukkit.getLogger().info("local definido");
				return;
			} else if(this.gameConfig.getConfigType() == GameConfigType.AREA3D) { 
				this.selection.setPointA(l);
				player.sendMessage("setPointA - config: " + this.gameConfig.getName() );
				if(this.selection.getPointB() == null) {
					player.sendMessage("Remember to select Area3D point B");
				}
			}

		}
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
    		Block block = event.getClickedBlock();
    		if(this.gameConfig.getConfigType() == GameConfigType.AREA3D) { 
				this.selection.setPointB(l);
				player.sendMessage("setPointB - config: " + this.gameConfig.getName() );
				if(this.selection.getPointA() == null) {
					player.sendMessage("Remember to select Area3D point A");
				}
			}
    	}
		
		if(this.gameConfig.getConfigType() == GameConfigType.AREA3D) {
			if(selection.getPointA() != null && selection.getPointB() != null) {
				this.configArenaValue = this.selection;
				player.sendMessage("Area defined: " + this.gameConfig.getName() );
				nextArenaConfig(player);
			}
		}
	}
	
}
