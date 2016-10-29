package br.com.minegames.core.hologram;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_10_R1.EntityArmorStand;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutSpawnEntityLiving;

public class HologramUtil {

	private static CopyOnWriteArrayList<EntityArmorStand> entitylist = new CopyOnWriteArrayList<EntityArmorStand>();
	private String[] Text;
	private Location location;
	private static double DISTANCE = 0.25D;
	private static int count;

	public HologramUtil(String[] Text, Location location) {
		this.Text = Text;
		this.location = location;
		create();
	}

	
	public void showPlayerTemp(JavaPlugin plugin, final Player p,int Time){
	    showPlayerx(p);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				hidePlayer(p);
			}
		}, Time);
	}
	
	
	public void showAllTemp(JavaPlugin plugin, final Player p,int Time){
		showAll();
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
			hideAll();
			}
		}, Time);
	}
	
	public static void showPlayerx(Player p) {
		
		for (EntityArmorStand armor : entitylist) {
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armor);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void showPlayer(Player p, String[] lines, Location l) {
		p.sendMessage("hide old holograms");
		hidePlayer(p);
		p.sendMessage("showPlayer");
		create(l, lines);
		for (EntityArmorStand armor : entitylist) {
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armor);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void hidePlayer(Player p) {
		p.sendMessage("entityList size: " + entitylist.size());
		int index = 0;
		for (EntityArmorStand armor : entitylist) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armor.getId());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
		entitylist.clear();
	}

	public void showAll() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			for (EntityArmorStand armor : entitylist) {
				PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armor);
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	public static void hideAll() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			for (EntityArmorStand armor : entitylist) {
				PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armor.getId());
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	private void create() {
		for (String Text : this.Text) {
			EntityArmorStand entity = new EntityArmorStand(((CraftWorld) this.location.getWorld()).getHandle(),this.location.getX(), this.location.getY(),this.location.getZ());
			entity.setCustomName(Text);
			entity.setCustomNameVisible(true);
			entity.setInvisible(true);
			entity.setNoGravity(true);
			entitylist.add(entity);
			this.location.subtract(0, this.DISTANCE, 0);
			count++;
		}

		for (int i = 0; i < count; i++) {
			this.location.add(0, this.DISTANCE, 0);
		}
		this.count = 0;
	}

	private static void create(Location l, String[] lines) {
		for (String Text : lines) {
			EntityArmorStand entity = new EntityArmorStand(((CraftWorld) l.getWorld()).getHandle(),l.getX(), l.getY(),l.getZ());
			entity.setCustomName(Text);
			entity.setCustomNameVisible(true);
			entity.setInvisible(true);
			entity.setNoGravity(true);
			entitylist.add(entity);
			l.subtract(0, DISTANCE, 0);
			count++;
		}

		for (int i = 0; i < count; i++) {
			l.add(0, DISTANCE, 0);
		}
		count = 0;
	}

}