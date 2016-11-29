package com.thecraftcloud.gamesetup.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.gamesetup.TheCraftCloudGameSetupPlugin;

public class ListAreasAction extends TheCraftCloudCommandAction {

	public ListAreasAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando List 3D Areas " + this.commandSender + " "
				+ "\n" + this.command 
				+ "\n" + this.arg2
				+ "\n" + this.arguments);
		
		Player player = null; 
		if( this.commandSender instanceof Player ) {
			player = (Player)commandSender;
			player.getInventory().addItem(new ItemStack(Material.WOOD_AXE));
		}else{
			return;
		}

		String filterName = "";
		if(arguments.length == 2) {
			filterName = arguments[1];
		}
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		TheCraftCloudGameSetupPlugin p = (TheCraftCloudGameSetupPlugin)plugin;
		Arena arena = delegate.findArena(p.getArena().getArena_uuid().toString());
		int i = 0;
		for(Area3D area: arena.getAreas()) {
			player.sendMessage((i++) + " - " + area.getName() + ":" + area.getType() + ":" + area.getPointA() + "/" + area.getPointB() );
		}
	}
}
