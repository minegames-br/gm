package com.thecraftcloud.plugin.command;

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

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.command.CommandAction;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.plugin.TheCraftCloudPlugin;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class LoadArenaAction extends CommandAction {

	private int threads = 3;
	
	public LoadArenaAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	@Override
	public void execute() {
		Bukkit.getLogger().info("Executando commando Load Arena " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		TheCraftCloudPlugin mgplugin = (TheCraftCloudPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudPlugin.THE_CRAFT_CLOUD_PLUGIN);
		Arena arena = mgplugin.getArena();
		Game game = mgplugin.getGame();
		
		if(arena.getArea() == null || arena.getArea().getPointA() == null || arena.getArea().getPointB() == null) {
			player.sendMessage("Game is not correctly configured yet. Try /mg setup");
			return;
		}
		
        File dir = mgplugin.getDataFolder();
    	File schematicFile = delegate.downloadArenaSchematic(arena.getArena_uuid(), dir.getAbsolutePath());
    	List<ExportBlock> arenaBlocks = new BlockManipulationUtil().loadSchematic( this.plugin , schematicFile, player);
    	mgplugin.startArenaBuild(player.getWorld().getName(), arenaBlocks);

	}

}
