package br.com.minegames.main;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;


import br.com.minegames.commands.cmdCreateNpc;
import br.com.minegames.npcs.JoinListener;
import br.com.minegames.npcs.NPC;


public class Main extends JavaPlugin implements Listener, PluginMessageListener {

	private cmdCreateNpc cmd;
	public CopyOnWriteArraySet<NPC> npcList = new CopyOnWriteArraySet<NPC>();
	private NPC npc;

	@Override
	public void onEnable() {
		loadConfiguration();
		System.out.print("[MGServerNPC] MGServerNPC plugin Enabled!");
		registerListeners();
		final JavaPlugin plugin = this;
		ProtocolManager protocolManager;
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		if(npcList.size() != 0) {
			
			protocolManager.addPacketListener(new  PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
				public void onPacketReceiving(PacketEvent event) {
					
					System.out.println(event.getPlayer());
					System.out.println(event.getPacketType());
					System.out.println(event.getPacket());
					System.out.println("BLOCK DATA"+ event.getPacket().getBlockData());
					System.out.println(event.getSource());
					
					if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
						
						
						PacketContainer packet = event.getPacket();
						Player player = event.getPlayer();
						
						//System.out.println(event.getPlayer().getWorld().getNearbyEntities(player.getLocation(), 2,2,2));
						//Collection<Entity> et = player.getWorld().getEntitiesByClasses(CraftPlayer.class);
						
						Collection<Entity> et = player.getWorld().getNearbyEntities(player.getLocation(), 2,2,2);
						if (!(et instanceof Player)) {
							System.out.print("name" + et);
							//Entity e = (Entity) et;
						}
							
						
				
					}
					
				}

			
			});
			
		/*	// Censor
			protocolManager
					.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
						@Override
						public void onPacketReceiving(PacketEvent event) {
							
							
		
							if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
								
						
								
								PacketContainer packet = event.getPacket();
								Player player = event.getPlayer();
						
								
								String server = (String) getConfig().get("1.npc.server"); 
								//ByteArrayDataOutput out = ByteStreams.newDataOutput();
								//out.writeUTF("Connect");
								//out.writeUTF("gungame");
								//player.sendPluginMessage(plugin, "BungeeCord",
								//out.toByteArray());
								player.sendMessage("Bem-vindo ao servidor " + server);
							}
						}
					}); */
		}
		
		// commands
		getCommand("mgnpc").setExecutor(new cmdCreateNpc(this));

	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new JoinListener(this), this);
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
		// TODO Auto-generated method stub
	}

	public void setProperty(String path, String value) {
		this.getConfig().set(path, value);
		this.saveConfig();
	}

	public void setPropertyInt(String path, Integer value) {
		this.getConfig().set(path, value);
		this.saveConfig();
	}

	public void loadConfiguration() {
		Integer nList = (Integer) getConfig().get("npcList");
		if (nList != null) {
			for (int i = 1; i <= nList; i++) {
				Integer n = (Integer) getConfig().get("npcList");
				String path = String.valueOf(i);
				String s = (String) getConfig().get(path + ".npc.server");
				String w = (String) getConfig().get(path + ".npc.world");
				String uuid = (String) getConfig().get(path + ".npc.uuid");
				String name = (String) getConfig().get(path + ".npc.name");
				Integer x = (Integer) getConfig().get(path + ".npc.X");
				Integer y = (Integer) getConfig().get(path + ".npc.Y");
				Integer z = (Integer) getConfig().get(path + ".npc.Z");

				Location location = new Location(Bukkit.getServer().getWorld(w), x, y, z);
				NPC npc = new NPC(name, UUID.fromString(uuid), location);
				System.out.println("NPC " + npc.getName());
				npcList.add(npc);

				this.getConfig().options().copyDefaults(true);
				this.saveConfig();
			}
		}
	}
}
