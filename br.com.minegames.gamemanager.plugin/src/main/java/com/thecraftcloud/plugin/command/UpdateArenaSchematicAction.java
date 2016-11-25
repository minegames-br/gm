package com.thecraftcloud.plugin.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Schematic;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;

public class UpdateArenaSchematicAction extends TheCraftCloudCommandAction {

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
		
		TheCraftCloudPlugin p = (TheCraftCloudPlugin)plugin;
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

		Arena arena = this.config.getArena();
		
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
