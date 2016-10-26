package br.com.minegames.gamemanager.plugin.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameArenaConfig;
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
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		String server_uuid = p.getConfigFile().getString("minegames.server.uuid");
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
		
		if(arguments.length != 3) {
			player.sendMessage("Please, choose a name. /mg createarena <name> <schematic>");
		}
		
		String name = arguments[1];
		String schematicFile = arguments[2];

		try{
			Schematic schematic = new Schematic();
			schematic.setName(schematicFile);
			schematic.setDescription(schematicFile);
			player.sendMessage("Creating schematic data...");
			schematic = delegate.createSchematic(schematic);
			
			WorldEditPlugin wep = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	        WorldEdit we = wep.getWorldEdit();			
	        
            File file = new File(wep.getDataFolder(), "/schematics/" + schematicFile + ".schematic");
			
			player.sendMessage("Uploading schematic...");
			delegate.uploadSchematic(schematic, file);
			
			Arena arena = new Arena();
			arena.setName(name);
			arena.setDescription(name);
			arena.setSchematic(schematic);
			player.sendMessage("Creating Arena data...");
			arena = delegate.createArena(arena);
			p.setArena(arena);
			
			Game game = p.getGame();
			
			GameArenaConfig gac = new GameArenaConfig();
			gac.setArena(arena);
			gac.setGame(game);
			
			player.sendMessage("Associating Game and Arena...");
			delegate.createGameArenaConfig(gac);
			
			player.sendMessage("You have created Arena: " + arena.getName() );
		}catch(Exception e) {
			e.printStackTrace();
			player.sendMessage("Ooops. I think we are broken.");
		}
		
	}
}
