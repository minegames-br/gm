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
import br.com.minegames.core.domain.Schematic;
import br.com.minegames.core.util.BlockManipulationUtil;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class UpdateArenaSchematicAction extends CommandAction {

	public UpdateArenaSchematicAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	@Override
	public void execute() {
		Bukkit.getLogger().info("Executando commando Update Arena Schematic " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();

		Arena arena = p.getArena();
		
		Schematic schematic = new Schematic();
		schematic.setName(arena.getName());
		schematic.setDescription("schematic da arena: " + arena.getDescription());
		player.sendMessage("Creating schematic data...");
		schematic = delegate.createSchematic(schematic);

		File dir = p.getDataFolder();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		new BlockManipulationUtil().exportSelection(player, p.getSelection(), dir);
		
        File file = new File(dir, "selection.blocks");
		
		player.sendMessage("Uploading schematic...");
		delegate.uploadSchematic(schematic, file);
		Area3D area = new Area3D();
		area = p.getSelection();
		
		area = delegate.addArea3D(area);
		
		arena.setArea(area);
		arena.setSchematic(schematic);
		player.sendMessage("Updating Arena's structure data...");
		arena = delegate.updateArena(arena);
		p.setArena(arena);
	}

}
