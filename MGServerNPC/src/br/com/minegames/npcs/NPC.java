package br.com.minegames.npcs;

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
import com.mojang.authlib.properties.Property;

import br.com.minegames.events.NpcSendToPlayerEvent;
import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class NPC {

	private static final Set<NPC> npcs = new HashSet<>();

	public static NPC[] getInstances() {

		return npcs.toArray(new NPC[npcs.size()]);
	}

	protected final String name;

	protected final UUID skin;

	protected Location location;

	public NPC(String name, UUID skin, Location location) {
		this.name = name;
		this.skin = skin;
		System.out.println(location);
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

		PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;

		GameProfile gp = new GameProfile(tempSkin, tempName);
		String value = "oiLBCnuUIfdpv3qCMPxtZLj/9MTx7V31rBejvGOYAzMoknjCi14fQOoUgc6TcXaTWmokYvYvO2IDYTDUnEtB4Dg08tuWbTapgIYQr6S7po6HpXbmwnO/zGi5EjaFyTUD46AxJlB1IVx6w5EzoUcefyjSlLLqaTPwkMUvTw4uaeZUja/McuRCKWSUP4rViQqtPVEZhQuJndhm4JgMsA7tq7IlFAjIL6q8GNtvWZLZAnsWi763gvljm/ZORRjdwo3riIAdfhcC2Frmz4PO0/96VF7oUkrAZ/umiCTZRx5ppLnxupMdqeVs0ARuCpfiFw01z65RTvvisj6Z8M73x33C4iuSkD1402TTBz7Z0f4KZhtLneTYzLE9693AV6/ASlPpFlHJTy6jPyU+3FFE9Go6xg+vXEPHNStn0sXxLY2zgCw1OhKbQBpQ2wyhMWfkKg0pSK4M2DtWJfyGO1Vh3JROPwZczVguzHsd4ugKHUZCy6XsdG6vTmSM5Z3WYm7gSpEPLFFYrbAbxRZeZhlruoMV0zkp3eU/3LkVtNLuTUnsiEcXIQzhHNnVZi3Pupcu8NAW0jIgmD0JMvJAR/kZec/k6GmDrEPq8k56B/WMs/YR9BFvs+YNbitxJ6SGASUTvriayDY4+tnXks8vfsrvr4SZ580stsvHZUI3VtRBzrhBzOY=";
		String signature = "eyJ0aW1lc3RhbXAiOjE0NzkzMjI4MjEzMDgsInByb2ZpbGVJZCI6IjEwYjZmNWFmNWVmNTQ2MTA5Nzc0YWMxMDYzMWFlNTVjIiwicHJvZmlsZU5hbWUiOiJfS2luZ0NyYWZ0Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zNTQ0YjRhM2NjN2RkYWMzN2Q2ZDYwMmNlNGExM2EyODlkYWQzMmQ2NDExODZlMTg2ODA1YjY0MTkyYWNlZTIifX19";
		gp.getProperties().put("textures", new Property("textures", value, signature));
		
		@SuppressWarnings("deprecation")
		EntityPlayer entityPlayer = new EntityPlayer(MinecraftServer.getServer(),
				MinecraftServer.getServer().getWorldServer(0), gp,
				new PlayerInteractManager(MinecraftServer.getServer().getWorld()));

		System.out.println(entityPlayer);
		System.out.println(entityPlayer.getWorld());
		
		new CraftHumanEntity((CraftServer) Bukkit.getServer(), entityPlayer).teleport(location);

		entityPlayer.displayName = tempName;
		entityPlayer.listName = new ChatComponentText("§");

		con.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
		con.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));

	}

}
