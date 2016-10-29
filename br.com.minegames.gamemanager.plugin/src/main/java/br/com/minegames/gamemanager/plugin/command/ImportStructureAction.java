package br.com.minegames.gamemanager.plugin.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.Sandstone;
import org.bukkit.material.Stairs;
import org.bukkit.material.Wool;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.export.ExportBlock;
import br.com.minegames.core.util.BlockManipulationUtil;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class ImportStructureAction extends CommandAction {

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
		
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		long time = System.currentTimeMillis();
		player.sendMessage("Reading blocks");
		File file = new File(p.getDataFolder(), "selection.blocks");
		BlockManipulationUtil.importSelection(file, player);
		player.sendMessage((System.currentTimeMillis()-time)/1000 + " secs import blocks");

	}

}
