package com.thecraftcloud.gungame.gyurix;
/**

import static com.thecraftcloud.gungame.GunGameConfig.tpBack;
import static java.lang.Math.max;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domain.GamePlayer;
import com.thecraftcloud.gungame.domain.GunGame;
import com.thecraftcloud.gungame.domain.GunGamePlayer;
import com.thecraftcloud.plugin.TheCraftCloudConfig;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

import gyurix.api.VariableAPI;
import gyurix.commands.Command;
import gyurix.configfile.ConfigFile;
import gyurix.sign.SignConfig;
import gyurix.spigotlib.GlobalLangFile;
import gyurix.spigotlib.GlobalLangFile.PluginLang;
import gyurix.spigotlib.SU;
import gyurix.spigotutils.LocationData;

 * Created by GyuriX on 2016. 09. 22..

public class GameControllerBackup extends TheCraftCloudMiniGameAbstract implements Listener {
    public static HashMap<String, Game> playerGames = new HashMap<>();
    public static HashMap<String, Location> lastLoc = new HashMap<>();
    public static PluginLang lang;
    public static ConfigFile kf;
    public static GameControllerBackup pl;
	private Integer gameDuration;

    @Override
    public void onEnable() {

		super.onEnable();
    	//ao criar, o jogo fica imediatamente esperando jogadores
		this.myCloudCraftGame = new GunGame();
		
        pl = this;
        SU.saveResources(this, "config.yml", "lang.yml");
        lang = GlobalLangFile.loadLF("gungame", getDataFolder() + File.separator + "lang.yml");
        kf = new ConfigFile(new File(getDataFolder() + File.separator + "config.yml"));
        kf.data.deserialize(GunGameConfig.class);
        SU.sch.scheduleSyncRepeatingTask(pl, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Game g = playerGames.get(p.getName());
                    if (g == null || g.state != GameState.Waiting && g.state != GameState.Starting)
                        continue;
                    Location last = lastLoc.get(p.getName());
                    if (last == null)
                        continue;
                    if (!p.getWorld().getName().equals(last.getWorld().getName()) || last.distance(p.getLocation()) > 0.01)
                        p.teleport(last);
                }
            }
        }, tpBack, tpBack);
        VariableAPI.handlers.put("gg", new VariableGG());
        //new CommandGG(this);
        SU.pm.registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent e) {
        Player plr = e.getPlayer();
        Game g = playerGames.get(plr.getName());
        if (g != null)
            e.setRespawnLocation(g.playerSpawns.get(plr.getName()).getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if (b.getType() != Material.SIGN_POST && b.getType() != Material.WALL_SIGN)
            return;
        LocationData ld = new LocationData(b);
        String gn = GunGameConfig.joinSigns.remove(ld);
        if (gn != null) {
            Game g = GunGameConfig.games.get(gn);
            if (g != null)
                g.signs.remove(ld);
            kf.save();
        }
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent e) {
        Block b = e.getBlock();
        boolean change = false;
        LocationData ld = new LocationData(b);
        String gn = GunGameConfig.joinSigns.remove(ld);
        if (gn != null) {
            Game g = GunGameConfig.games.get(gn);
            if (g != null)
                g.signs.remove(ld);
            change = true;
        }
        SignConfig create = GunGameConfig.signs.get("create");
        String arena = null;
        String[] lines = e.getLines();
        for (int i = 0; i < 4; i++) {
            String cl = create.lines.get(i);
            if (cl.equals("<arena>"))
                arena = lines[i];
            else if (!cl.equals(lines[i])) {
                if (change)
                    kf.save();
                return;
            }
        }
        Game g = arena == null ? null : GunGameConfig.games.get(arena);
        if (g != null) {
            GunGameConfig.joinSigns.put(ld, g.name);
            g.signs.add(ld);
            change = true;
        }
        if (change)
            kf.save();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Block b = e.getClickedBlock();
        if (b.getType() != Material.WALL_SIGN && b.getType() != Material.SIGN_POST)
            return;
        LocationData ld = new LocationData(b);
        String gn = GunGameConfig.joinSigns.get(ld);
        if (gn == null)
            return;
        Game g = GunGameConfig.games.get(gn);
        if (g != null)
            g.join(e.getPlayer());
    }

    @Override
    public void onDisable() {
        for (Game g : GunGameConfig.games.values())
            g.kill();
        GlobalLangFile.unloadLF(lang);
        VariableAPI.handlers.remove("gg");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player plr = e.getPlayer();
        lastLoc.remove(plr.getName());
        Game g = playerGames.remove(plr.getName());
        if (g != null)
            g.quit(plr);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        final Player plr = e.getEntity();
        SU.sch.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                plr.spigot().respawn();
            }
        });
        Game g = playerGames.get(plr.getName());
        if (g == null)
            return;
        g.levels.put(plr.getName(), max(1, g.levels.get(plr.getName()) - 1));
        Player killer = plr.getKiller();
        if (killer != null) {
            Integer level = g.levels.get(killer.getName());
            if (level != null) {
                g.levels.put(killer.getName(), level + 1);
                ArrayList<Command> cmds=GunGameConfig.levelups.get(level+1);
                if (cmds==null)
                    cmds=GunGameConfig.levelups.get(0);
                Command.executeAll(killer,cmds);
            }
        }
    }


	@Override
	public void startGameEngine() {
		super.startGameEngine();
		
		String gungame1 = "gungame1";
		Game gGame = GunGameConfig.games.get(gungame1);
		Bukkit.getLogger().info("gGame: " + gGame);
		
		for(GamePlayer gp: this.livePlayers) {
			gGame.join(gp.getPlayer());
		}
		
	}
	
	@Override
	public boolean shouldEndGame() {
    	//Terminar o jogo caso não tenha mais jogadores
    	if( this.getLivePlayers().size() == 0  && this.myCloudCraftGame.isStarted()) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6EndGameTask - No more players"));
            return true;
    	}
    	
    	//Terminar o jogo caso tenha alcançado o limite de tempo
    	long currentTime = System.currentTimeMillis();
    	long duration = currentTime - this.getMyCloudCraftGame().getGameStartTime();
		this.gameDuration = (Integer)this.getGameConfigInstance(TheCraftCloudConfig.GAME_DURATION_IN_SECONDS);

    	if( duration >= this.gameDuration  ) {
    		return true;
    	}
    	return false;
	}

	@Override
	public boolean isLastLevel() {
		return false;
	}

	@Override
	public void levelUp() {
		return;
	}

	@Override
	public GamePlayer createGamePlayer() {
		return new GunGamePlayer();
	}

	@Override
	public Integer getStartCountDown() {
		return this.countDown;
	}

	@Override
	public void setStartCountDown() {
		this.countDown = (Integer)this.getGameConfigInstance(TheCraftCloudConfig.START_COUNTDOWN);
	}


	@Override
	public Local getLobby() {
		return this.lobbyLocal;
	}

	@Override
	public void setLobby() {
		Local l = (Local)this.getGameArenaConfig(TheCraftCloudConfig.LOBBY_LOCATION);
		this.lobbyLocal = l;
	}

	@Override
	public Integer getMinPlayers() {
		Integer minPlayers = (Integer)this.getGameConfigInstance(TheCraftCloudConfig.MIN_PLAYERS);
		return minPlayers;
	}

	@Override
	public Integer getMaxPlayers() {
		Integer maxPlayers = (Integer)this.getGameConfigInstance(TheCraftCloudConfig.MAX_PLAYERS);
		return maxPlayers;
	}

	

}
 */