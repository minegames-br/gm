package br.com.minegames.gamemanager.plugin.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.minegames.core.command.CommandAction;
import br.com.minegames.core.logging.Logger;

public class MineGamesCommand  implements CommandExecutor {

	private JavaPlugin plugin;
	private HashMap<String,Class> commandArgs = new HashMap<String, Class>();
	
    public MineGamesCommand(JavaPlugin plugin) {
		super();
		this.plugin = plugin;
		init();
	}
    
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Logger.debug( "CommandSender " + arg0.getName() );
		Logger.debug( "Command " + arg1.getName() + " " + arg1.getLabel() + " " + arg1.getDescription() );
		Logger.debug( "arg2 " + arg2);
		Logger.info( "args length: " + arg3.length );
		for(String arg: arg3) {
			Logger.info( "arg: " + arg );
		}
		
		CommandAction action = getAction(arg0, arg1, arg2, arg3);
		action.execute();
		return false;
	}
	
	private void init() {
		commandArgs.put("register", RegisterServerAction.class);
	}
	
	private CommandAction getAction(CommandSender sender, Command command, String arg2, String[] arg3) {
		CommandAction action = null;
		String actionName = arg3[0];
		
		try {
			for(String key: commandArgs.keySet()) {
				Logger.info(key + " " + actionName );
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
