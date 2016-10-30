package br.com.minegames.gamemanager.plugin.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.logging.MGLogger;

public class MineGamesCommand  implements CommandExecutor {

	private JavaPlugin plugin;
	private HashMap<String,Class> commandArgs = new HashMap<String, Class>();
	
    public MineGamesCommand(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
		init();
	}
    
	@Override
	public boolean onCommand(CommandSender commandSender, Command arg1, String arg2, String[] arg3) {
		MGLogger.debug( "CommandSender " + commandSender.getName() );
		MGLogger.debug( "Command " + arg1.getName() + " " + arg1.getLabel() + " " + arg1.getDescription() );
		MGLogger.debug( "arg2 " + arg2);
		MGLogger.info( "args length: " + arg3.length );
		for(String arg: arg3) {
			MGLogger.info( "arg: " + arg );
		}
		
		final CommandAction action = getAction(commandSender, arg1, arg2, arg3);
		Player player = null; 
		if( commandSender instanceof Player ) {
			player = (Player)commandSender;
		}
		
		if(action == null) {
			player.sendMessage("Invalid Command");
			return false;
		}
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
        		action.execute();
            }
        }, 20L);
		return false;
	}
	
	private void init() {
		commandArgs.put("register", RegisterServerAction.class);
		commandArgs.put("select", SelectAreaAction.class);
		commandArgs.put("addarea", DefineAreaAction.class);
		commandArgs.put("listareas", ListAreasAction.class);
		commandArgs.put("listarenas", ListArenasAction.class);
		commandArgs.put("setarena", DefineArenaAction.class);
		commandArgs.put("createarena", CreateArenaAction.class);
		commandArgs.put("export", ExportSelectAction.class);
		commandArgs.put("destroy", DestroySelectionAction.class);
		commandArgs.put("import", ImportStructureAction.class);
		commandArgs.put("hologram", ShowHologramAction.class);
		commandArgs.put("listgames", ListGamesAction.class);
		commandArgs.put("setgame", DefineGameAction.class);
		commandArgs.put("setup", SetupGameArenaAction.class);
		commandArgs.put("npc", SpawnNPCAction.class);
	}
	
	private CommandAction getAction(CommandSender sender, Command command, String arg2, String[] arg3) {
		CommandAction action = null;
		String actionName = arg3[0];
		
		try {
			for(String key: commandArgs.keySet()) {
				MGLogger.info(key + " " + actionName );
				if(key.equalsIgnoreCase(actionName)) {
					Class c = commandArgs.get(key);
					Constructor cons = c.getConstructor(JavaPlugin.class, CommandSender.class, Command.class, String.class, String[].class);
					action = (CommandAction) cons.newInstance(this.plugin, sender, command, arg2, arg3);
					break;
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return action;
	}


}
