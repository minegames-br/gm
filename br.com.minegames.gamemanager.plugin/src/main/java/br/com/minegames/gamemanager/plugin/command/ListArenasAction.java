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
import br.com.minegames.core.domain.Arena;
import br.com.minegames.gamemanager.client.GameManagerDelegate;
import br.com.minegames.gamemanager.plugin.MineGamesPlugin;

public class ListArenasAction extends CommandAction {

	public ListArenasAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		super(plugin, arg0, arg1, arg2, arguments);
	}

	public void execute() {
		Bukkit.getLogger().info("Executando commando List Arenas " + this.commandSender + " "
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

		String filterName = arguments[0];
		GameManagerDelegate delegate = GameManagerDelegate.getInstance();
		MineGamesPlugin p = (MineGamesPlugin)plugin;
		List<Arena> arenas = delegate.findArenas(filterName);
		p.setArenas(arenas);
		int i = 0;
		for(Arena arena: arenas) {
			player.sendMessage((i++) + " - " + arena.getName() );
		}
	}
}
