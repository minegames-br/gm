package com.thecraftcloud.gamesetup.command;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class ImportStructureAction extends TheCraftCloudCommandAction {

	private int threads = 3;
	
	public ImportStructureAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	@Override
	public void execute() {
		Bukkit.getLogger().info("Executando commando Import Structure " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)plugin;
		long time = System.currentTimeMillis();
		player.sendMessage("Reading blocks");
		File file = new File(p.getDataFolder(), "selection.blocks");
		List<ExportBlock> blocks = new BlockManipulationUtil().loadSchematic(this.plugin, file, player);
		p.setArenaBlocks(blocks);
		p.startArenaBuild(player.getWorld().getName(), blocks);
		player.sendMessage((System.currentTimeMillis()-time)/1000 + " secs import blocks");

	}

}
