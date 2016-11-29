package com.thecraftcloud.gamesetup.command;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.export.ExportBlock;
import com.thecraftcloud.core.util.BlockManipulationUtil;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class LoadArenaAction extends TheCraftCloudCommandAction {

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
		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)Bukkit.getPluginManager().getPlugin(TheCraftCloudGameSetupPlugin.THE_CRAFT_CLOUD_PLUGIN);
		Arena arena = p.getArena();
		Game game = p.getGame();
		
		if(arena.getArea() == null || arena.getArea().getPointA() == null || arena.getArea().getPointB() == null) {
			player.sendMessage("Game is not correctly configured yet. Try /mg setup");
			return;
		}
		
        File dir = p.getDataFolder();
    	File schematicFile = delegate.downloadArenaSchematic(arena.getArena_uuid(), dir.getAbsolutePath());
    	List<ExportBlock> arenaBlocks = new BlockManipulationUtil().loadSchematic( this.plugin , schematicFile, player);
    	p.startArenaBuild(player.getWorld().getName(), arenaBlocks);

	}

}
