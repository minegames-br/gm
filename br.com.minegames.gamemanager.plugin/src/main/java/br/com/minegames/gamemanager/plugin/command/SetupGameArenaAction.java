package br.com.minegames.gamemanager.plugin.command;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.GameConfig;
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

		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		
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
		
		player.sendMessage("Loading game configurations already set up...");
		List<GameConfigInstance> listGCI = delegate.findAllGameConfigInstanceByGameUUID(p.getGame().getGame_uuid().toString());
		for(GameConfigInstance gci: listGCI) {
			p.getGameGameConfigMap().put(gci.getGameConfig().getName(), gci);
		}
		
		List<GameArenaConfig> listGCA = delegate.findAllGameConfigArenaByGameArena(p.getGame().getGame_uuid().toString(), p.getArena().getArena_uuid().toString());
		for(GameArenaConfig gca: listGCA) {
			p.getGameConfigArenaMap().put(gca.getGameConfig().getName(), gca);
		}
		
		player.sendMessage("Loading game data to be set up...");
		List<GameConfig> list = delegate.listGameConfig(p.getGame());
		Bukkit.getLogger().info("game config list: " + list.size());
		
		p.setConfigList(list);
		p.setPlayer(player);
		p.setupGameArenaConfig(player);
	}
	
}
