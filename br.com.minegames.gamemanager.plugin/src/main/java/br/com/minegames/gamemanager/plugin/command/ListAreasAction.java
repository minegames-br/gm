package br.com.minegames.gamemanager.plugin.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class ListAreasAction extends CommandAction {

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
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		Arena arena = delegate.findArena(p.getArena().getArena_uuid().toString());
		int i = 0;
		for(Area3D area: arena.getAreas()) {
			player.sendMessage((i++) + " - " + area.getName() + ":" + area.getType() + ":" + area.getPointA() + "/" + area.getPointB() );
		}
	}
}
