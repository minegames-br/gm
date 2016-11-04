package br.com.minegames.gamemanager.plugin.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.Schematic;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class CreateArenaAction extends CommandAction {

	public CreateArenaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando Create Arena " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		
		if(p.getSelection() == null) {
			player.sendMessage("Please, select the arena 3D area first: /mg select");
			return;
		}
		
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		String server_uuid = p.getConfig().getString("thecraftcloud.server.uuid");
		if(server_uuid == null || server_uuid.equals("")) {
			if(player != null) {
				player.sendMessage("Please, register server first. /mg register <name>");
				return;
			}
		}

		if(p.getGame() == null) {
			player.sendMessage("Please, select the Game first. /mg listgames then /mg setgame <#>");
			return;
		}
		
		if(arguments.length != 2) {
			player.sendMessage("Please, choose a name. /mg createarena <name>");
		}
		
		String name = arguments[1];

		try{
			Schematic schematic = new Schematic();
			schematic.setName(name);
			schematic.setDescription(name);
			player.sendMessage("Creating schematic data...");
			schematic = delegate.createSchematic(schematic);
			
			File dir = p.getDataFolder();
			if(!dir.exists()) {
				dir.mkdirs();
			}
            File file = new File(dir, "selection.blocks");
			
			player.sendMessage("Uploading schematic...");
			delegate.uploadSchematic(schematic, file);
			
			Arena arena = new Arena();
			arena.setName(name);
			arena.setDescription(name);
			arena.setSchematic(schematic);

			player.sendMessage("Creating Area3D data...");
			Area3D area = delegate.addArea3D(p.getSelection());
			arena.setArea(area);
			arena = delegate.updateArena(arena);
			
			player.sendMessage("Creating Arena data...");
			arena = delegate.createArena(arena);
			p.setArena(arena);
			
			player.sendMessage("You have created arena: " + arena.getName() );
		}catch(Exception e) {
			e.printStackTrace();
			player.sendMessage("Ooops. I think we are broken.");
		}
		
	}
}
