package br.com.minegames.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.minegames.main.Main;
import br.com.minegames.npcs.NPC;

public class cmdCreateNpc implements CommandExecutor {

	private Main plugin;
	private Player player;
	public NPC npc;
	
	public cmdCreateNpc(Main plugin) {
		super();
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		player = (Player) sender;

		if (CommandLabel.equalsIgnoreCase("mgnpc")) {
			if (args.length == 0) {
				sender.sendMessage("Please set -> /mgnpc <npcName> <serverName> <uuid>");
			} else if (args.length == 3) {
				String name = args[0];
				String server = args[1];
				String uuid = args[2];

				System.out.println(args[0] + " " + args[1] + " " + args[2]);

				if (args[0].equalsIgnoreCase(name)) {
					if (args[1].equalsIgnoreCase(server)) {
						if (args[2].equalsIgnoreCase(uuid)) {
							sender.sendMessage("NPC was created");

							npc = new NPC(name, UUID.fromString(uuid), player.getLocation());
							int listSize = addToNpcList(npc);
						
							setValues(listSize, server, player.getWorld().getName(), uuid, name, player.getLocation().getBlockX(),
									player.getLocation().getBlockY(), player.getLocation().getBlockZ());

							Bukkit.getConsoleSender().sendMessage("config file created: " + uuid + " " + name);
						}
					}
				}
			}

		}
		return false;
	}

	public void setValues(int listSize, String server, String world, String uuid, String name, int x, int y, int z) {
		String npcList = String.valueOf(listSize);	
		plugin.setPropertyInt("npcList", listSize);
		plugin.setProperty(npcList + ".npc" + ".server", server);
		plugin.setProperty(npcList + ".npc" + ".world", world);
		plugin.setProperty(npcList + ".npc" + ".uuid", uuid);
		plugin.setProperty(npcList + ".npc" + ".name", name);
		plugin.setPropertyInt(npcList + ".npc" + ".X", x);
		plugin.setPropertyInt(npcList + ".npc" + ".Y", y);
		plugin.setPropertyInt(npcList + ".npc" + ".Z", z);

	}
	
	public int addToNpcList(NPC npc) {
		plugin.npcList.add(npc);
		System.out.println(plugin.npcList.size());
		return plugin.npcList.size();
	}
}
