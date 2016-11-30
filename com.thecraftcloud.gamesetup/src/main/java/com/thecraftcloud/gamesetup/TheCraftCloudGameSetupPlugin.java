package com.thecraftcloud.gamesetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameConfigScope;
import com.thecraftcloud.core.domain.GameConfigType;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.hologram.HologramUtil;
import com.thecraftcloud.core.multiverse.MultiVerseWrapper;
import com.thecraftcloud.core.util.LocationUtil;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.core.util.title.TitleUtil;
import com.thecraftcloud.gamesetup.command.TheCraftCloudCommand;
import com.thecraftcloud.gamesetup.listener.BlockBreakListener;
import com.thecraftcloud.gamesetup.listener.PlayerOnClick;
import com.thecraftcloud.gamesetup.task.ArenaSetupTask;
import com.thecraftcloud.gamesetup.task.BuildArenaTask;

public class TheCraftCloudGameSetupPlugin extends JavaPlugin {

	public static String THE_CRAFT_CLOUD_PLUGIN = "TheCraftCloud-Plugin";
	public static Integer TIME_SET_SUNRISE = 24000;
	public static Integer TIME_SET_SUNRISE2 = 500;
	public static Integer TIME_SET_DAY = 1000;
	public static Integer TIME_SET_SUNSET = 11615;
	public static Integer TIME_SET_SUNSET2 = 12500;
	public static Integer TIME_SET_NIGHT = 13000;
	public static Integer TIME_SET_NIGHT2 = 14000;
	
	
	private Area3D selection;
	
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

	protected List<ExportBlock> arenaBlocks;
	protected int indexBlock = 0;
	
	protected BuildArenaTask buildArenaTask;
	protected List<Integer> threadIds;
	
	private Boolean setupArena = false;
	
	private int indexConfig = 0;
	private Object configValue;

	private int indexArenaConfig = 0;
	private Object configArenaValue;

	private GameArenaConfig gac;
	private GameConfigInstance gci;

	private ArenaSetupTask arenaSetupTask;
	private int arenaSetupTaskThreadID;

	private int tccAdminTaskThreadID;

	private LocationUtil locationUtil = new LocationUtil();
	private boolean setup;
	private Arena arena;
	
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
	private Game game;
	private ServerInstance server;
	private String server_uuid;
	
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
	    loadConfiguration();
	    System.out.print("[TheCraftCloud] TheCraftCloud Plugin Enabled!");
		
		getCommand("tcc").setExecutor(new TheCraftCloudCommand(this));
		
	    registerListeners();

	    BukkitScheduler scheduler = Bukkit.getScheduler();
	   // this.tccAdminTask = new TheCraftCloudAdminTask(this);
		//this.tccAdminTaskThreadID = scheduler.scheduleSyncRepeatingTask(this, this.tccAdminTask, 1L, 600L);
	}
	
	@Override
	public void onDisable() {
	    BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.cancelTask( this.tccAdminTaskThreadID );
		
		Bukkit.getConsoleSender().sendMessage(Utils.color("&3TheCraftCloudPlugin disabled successfully"));
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
		//Fazer o Y ser um bloco acima do menor Y 
		updateYPosition();
	}

	/**
	 * Esse método corrige o Y para evitar, por exemplo, de um zumbi nascer dentro de um bloco
	 */
	private void updateYPosition() {
		/*
		if(this.selection.getPointB() != null && this.selection.getPointA() != null) {
			if(this.selection.getPointA().getY() <= this.selection.getPointB().getY() ) {
				this.selection.getPointA().setY( this.selection.getPointA().getY() +1 );
			} else {
				this.selection.getPointB().setY( this.selection.getPointB().getY() +1 );
			}
		}
		*/
	}

	public void setSelectionPointB(Location l) {
		Local local = new Local(l.getBlockX(), l.getBlockY(), l.getBlockZ());
		if(selection == null) {
			this.selection = new Area3D();
		}
		selection.setPointB(local);
		//Fazer o Y ser um bloco acima do menor Y 
		updateYPosition();
	}

	public boolean isServerRegistered() {
		String value = this.getConfig().getString("thecraftcloud.server.uuid");
		if(value != null && !value.trim().equals("")) {
			return false;
		}
		
		return false;
	}

	public List<Game> getGames() {
		return this.games;
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
		if(!this.setupArena) {
			return;
		}
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		final TheCraftCloudGameSetupPlugin plugin = this;

	}

	public void setupGameArenaConfig(Player player) {
		this.setupLocation = new Location(player.getWorld(), -764, 5, 402);
		this.player = player;
		player.teleport(this.setupLocation);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setGameMode(GameMode.CREATIVE);
		
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
		
		TitleUtil.sendTitle(player, 10, 20, 10, "Click on levers to change value and switch configs.", "Open the door to save.");
		
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
		
		this.selection = null;
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
				this.gac.setArena(this.getArena() );
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
	
	public Arena getArena() {
		return this.arena;
	}

	public void updateConfig( ) {
		Object configObject = this.getConfigValue();
		
		if(configObject instanceof Integer) {
			Bukkit.getConsoleSender().sendMessage("&6updateConfig: "  + this.gameConfig.getConfigScope() + " " + this.gameConfig.getName() + ": " + configObject.toString());
			String key = this.getGameConfig().getName();
			if(this.gameConfig.getConfigScope() == GameConfigScope.ARENA) {
				this.gac.setArena(this.getArena() );
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

			Bukkit.getLogger().info("GameConfig: " + this.gameConfig.getName() + " Scope:" + this.gameConfig.getConfigScope() + " type: " + gameConfig.getConfigType() );
       		if(gameConfig.getConfigScope() == GameConfigScope.GLOBAL) {
       			if(gci.getGameConfig().getGame_config_uuid() == null) {
       				delegate.createGameConfigInstance(gci);
       			} else {
       				delegate.updateGameConfigInstance(gci);
       			}
       		} else {
            	if(gac.getGac_uuid() == null) {
	            	delegate.createGameArenaConfig(gac);
            	} else {
            		delegate.updateGameArenaConfig(gac);
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

	public TheCraftCloudDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(TheCraftCloudDelegate delegate) {
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
		Location loc = locationUtil .getMiddle(player.getWorld(), this.getArena().getArea());
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
	
	public void cancelArenaSetupTask( ) {
		Bukkit.getLogger().info("cancelArenaSetupTask");
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.cancelTask( this.arenaSetupTaskThreadID );
		this.setupArena = false;
		this.setupLocation = new Location(player.getWorld(), -764, 5, 402);
		player.teleport(this.setupLocation);

		player.getInventory().setItemInMainHand(null);
		this.setupArena = false;
		this.selection = null;
		this.configArenaValue = null;
		player.sendMessage("Setup has been cancelled");
	}
	
	public void completeArenaSetupTask() {
		Bukkit.getLogger().info("completeArenaSetupTask");
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.cancelTask( this.arenaSetupTaskThreadID );
		this.setupLocation = new Location(player.getWorld(), -764, 5, 402);
		player.teleport(this.setupLocation);
		saveGameConfig();
		this.setupArena = false;
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
				player.sendMessage("point B - " + this.selection.getPointB() );
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
				player.sendMessage("point A - " + this.selection.getPointA() );
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
	
	public void startArenaBuild(String arenaName, List<ExportBlock> arenaBlocks) {
		this.world = Bukkit.getWorld(arenaName);
		if(this.world == null) {
	        //criar mundo void para tentar ver se a recriacao da arena fica pronta mais rapidamente
			String worldName = "" + System.currentTimeMillis();
			Bukkit.getLogger().info("cloneWorld void para " + worldName);
			this.world = new MultiVerseWrapper().createWorldVoid( worldName );
		}
		this.setArenaBlocks(arenaBlocks);

		Bukkit.getConsoleSender().sendMessage("&6startArenaBuild: " + arenaBlocks.size() + " blocks");
		this.indexBlock = 0;
		BukkitScheduler scheduler = Bukkit.getScheduler();
		this.buildArenaTask = new BuildArenaTask(this);
		this.threadIds = new CopyOnWriteArrayList<Integer>();
		for(int i = 0; i < 5; i++) {
			Integer _buildArenaThreadID = scheduler.scheduleSyncRepeatingTask(this, this.buildArenaTask, 5L, 200L);
			this.threadIds.add(_buildArenaThreadID);
		}
	}
	
	public void setArenaBlocks(List<ExportBlock> arenaBlocks) {
		this.arenaBlocks = arenaBlocks;
	}

	public void completeBuildArenaTask() {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		for(int i = 0; i < threadIds.size(); i++) {
			scheduler.cancelTask( threadIds.get(i) );
		}
		threadIds.clear();
	}

	public ExportBlock getNextBlock() {
		if(indexBlock < arenaBlocks.size()) {
			ExportBlock block = arenaBlocks.get(indexBlock);
			indexBlock++;
			return block;
		} else {
			return null;
		}
	}

	public World getWorld() {
		return this.world;
	}

	public void loadConfiguration(){
	     //See "Creating you're defaults"
	     this.getConfig().options().copyDefaults(true); // NOTE: You do not have to use "plugin." if the class extends the java plugin
	     //Save the config whenever you manipulate it
	     this.saveConfig();
	     
	}

	public void switchArenaTime() {
		if(!this.isSetup()) {
			return;
		}
		
		final Arena arena = this.getArena();
		
		if( arena.getTime() != null) {
			Integer time = arena.getTime();
			if(time.equals(TIME_SET_SUNRISE)) {
				arena.setTime(TIME_SET_SUNRISE2);
			} else if(time.equals(TIME_SET_SUNRISE2)) {
				arena.setTime(TIME_SET_DAY);
			} else if(time.equals(TIME_SET_DAY)) {
				arena.setTime(TIME_SET_SUNSET);
			}else if(time.equals(TIME_SET_SUNSET)) {
				arena.setTime(TIME_SET_SUNSET2);
			}else if(time.equals(TIME_SET_SUNSET2)) {
				arena.setTime(TIME_SET_NIGHT);
			}else if(time.equals(TIME_SET_NIGHT)) {
				arena.setTime(TIME_SET_NIGHT2);
			}else if(time.equals(TIME_SET_NIGHT2)) {
				arena.setTime(TIME_SET_SUNRISE);
			} else {
				arena.setTime(TIME_SET_SUNRISE);
			}
		} else {
			arena.setTime(TIME_SET_SUNRISE);
		}
		this.player.getWorld().setTime(arena.getTime());
		//this.world.setTime(arena.getTime());
		player.sendMessage("Time Set: " + arena.getTime() );
		HologramUtil.showPlayer( player, new String[]{ "Time Set", arena.getTime().toString()}, new Location(player.getWorld(), -766, 4, 397) );
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable(){
			public void run() {
				delegate.updateArena(arena);
			}
		});
	}

	public void setSetup(boolean b) {
		this.setup = b;
	}
	
	public boolean isSetup() {
		return this.setup;
	}

	public Game getGame() {
		return this.game;
	}

	public ServerInstance getServerInstance() {
		return this.server;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getServer_uuid() {
		return this.server_uuid;
	}

	public void setServer(ServerInstance server2) {
		this.server = server2;
	}

	public void setProperty(String path, String value) {
		this.getConfig().set(path, value);
		this.saveConfig();
	}


}