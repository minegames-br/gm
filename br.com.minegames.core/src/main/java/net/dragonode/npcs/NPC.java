package net.dragonode.npcs;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.dragonode.npcs.events.NpcSendToPlayerEvent;
import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class NPC {
	
	private static final Set<NPC> 
			npcs = new HashSet<>();
	
	public static NPC[] getInstances() {
		
		return npcs.toArray(new NPC[npcs.size()]);
	}
	
	protected final String
			name;
	
	protected final UUID
			skin;

	protected Location
			location;
	
	public NPC(String name, UUID skin, Location location) {
		this.name = name;
		this.skin = skin;
		
		this.location = location;
		
		npcs.add(this);
		for (Player player : Bukkit.getOnlinePlayers())
			update(player);
		
		
	}
	
	public NPC(UUID skin, Location location) {
		this("", skin, location);
		
	}
	
	public final UUID getSkin() {
		
		return skin;
	}
	
	public final String getName() {
		
		return name;
	}
	
	public final void update(Player player) {
	
		String tempName;
		UUID tempSkin;
		
		NpcSendToPlayerEvent event = new NpcSendToPlayerEvent(this, player);
		Bukkit.getPluginManager().callEvent(event);
		
		tempName = event.getName();
		tempSkin = event.getSkin();
		
		PlayerConnection con = 
				((CraftPlayer)player).getHandle().playerConnection;
	
		GameProfile gp = new GameProfile(tempSkin, tempName);
		@SuppressWarnings("deprecation")
		EntityPlayer entityPlayer =  new EntityPlayer(MinecraftServer.getServer(), 
				MinecraftServer.getServer().getWorldServer(0), 
				gp, new PlayerInteractManager(MinecraftServer.getServer().getWorld()));

		new CraftHumanEntity((CraftServer) Bukkit.getServer(), entityPlayer).teleport(location);
		
		entityPlayer.displayName = tempName;
		entityPlayer.listName = new ChatComponentText("§");
		
		con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
		con.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
		
	}
}
