package com.thecraftcloud.plugin.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class SetupGameArenaAction extends TheCraftCloudCommandAction {

	
	public SetupGameArenaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Setup " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		TheCraftCloudPlugin p = (TheCraftCloudPlugin)this.plugin;

		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		
		if(p.isServerRegistered()) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
			}
			return;
		}
		
		if(this.config.getArena() == null ) {
			if(player != null) {
				player.sendMessage("Choose one of the existing arenas: /mg listarenas [name] or create one /mg createarena <name>");
			}
			return;
		}
		
		if(this.config.getGame() == null) {
			if(player != null) {
				player.sendMessage("Please, select the game first: /mg listgames and /mg setgame <game>");
			}
			return;
		}
		
		player.sendMessage("Loading game configurations already set up...");
		List<GameConfigInstance> listGCI = delegate.findAllGameConfigInstanceByGameUUID(this.config.getGame().getGame_uuid().toString());
		for(GameConfigInstance gci: listGCI) {
			p.getGameGameConfigMap().put(gci.getGameConfig().getName(), gci);
		}
		
		List<GameArenaConfig> listGCA = delegate.findAllGameConfigArenaByGameArena(this.config.getGame().getGame_uuid().toString(), this.config.getArena().getArena_uuid().toString());
		for(GameArenaConfig gca: listGCA) {
			p.getGameConfigArenaMap().put(gca.getGameConfig().getName(), gca);
		}
		
		player.sendMessage("Loading game data to be set up...");
		List<GameConfig> list = delegate.listGameConfig(this.config.getGame());
		Bukkit.getLogger().info("game config list: " + list.size());
		
		p.setSetup(true);
		p.setConfigList(list);
		p.setPlayer(player);
		p.setupGameArenaConfig(player);
	}
	
}
