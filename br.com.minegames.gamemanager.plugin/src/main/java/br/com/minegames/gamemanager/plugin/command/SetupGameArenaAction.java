package br.com.minegames.gamemanager.plugin.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameConfigScope;
import br.com.minegames.core.domain.GameConfigInstance;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class SetupGameArenaAction extends CommandAction {

	
	public SetupGameArenaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Register Server " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}

		MineGamesPlugin p = (MineGamesPlugin)this.plugin;

		String server_uuid = p.getConfigFile().getString("minegames.server.uuid");
		if(server_uuid == null || server_uuid.equals("")) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
			}
			return;
		}
		
		if(p.getArena() == null ) {
			if(player != null) {
				player.sendMessage("Choose one of the existing arenas: /mg listarenas [name] or create one /mg createarena <name>");
			}
			return;
		}
		
		if(p.getGame() == null) {
			if(player != null) {
				player.sendMessage("Please, select the game first: /mg listgames and /mg setgame <game>");
			}
			return;
		}
		
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		
		List<GameConfig> list = delegate.listGameConfig(p.getGame());
		for(GameConfig gc: list) {
			if(gc.getConfigScope() == GameConfigScope.ARENA) {
				p.getGameConfigArenaMap().put(gc.getName(), new GameArenaConfig());
			} else {
				p.getGameGameConfigMap().put(gc.getName(), new GameConfigInstance());
			}
		}
		p.setConfigList(list);
		p.setupGameArenaConfig(player);
	}
	
}
