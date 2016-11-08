package com.thecraftcloud.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandAction {
	
	protected JavaPlugin plugin;
	protected CommandSender commandSender;
	protected Command command;
	protected String arg2;
	protected String[] arguments;

	public CommandAction(JavaPlugin plugin, CommandSender arg0, Command arg1, String arg2, String[] arguments) {
		this.plugin=plugin;
		this.commandSender = arg0;
		this.command = arg1;
		this.arg2 = arg2;
		this.arguments = arguments;
	}
	

	public JavaPlugin getPlugin() {
		return plugin;
	}
	public void setPlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public CommandSender getCommandSender() {
		return commandSender;
	}
	public void setCommandSender(CommandSender commandSender) {
		this.commandSender = commandSender;
	}
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public String getArg2() {
		return arg2;
	}
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}
	public String[] getArguments() {
		return arguments;
	}
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public abstract void execute();
	
}
