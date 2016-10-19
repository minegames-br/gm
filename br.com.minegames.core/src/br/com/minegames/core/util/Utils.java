package br.com.minegames.core.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Local;

public class Utils {

    private Utils() {
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
    public static void shootFirework(Player player) {
        Location location = player.getLocation();
    	shootFirework(location);
    }

    public static Firework shootFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
        
        data.addEffects(FireworkEffect.builder().withColor(Color.GREEN).with( FireworkEffect.Type.BALL_LARGE).build());
        data.setPower(2);
        
        firework.setFireworkMeta(data);
    	
    	return firework;
    }

	public static Location toLocation(World world, Area3D area) {
		Location l = new Location(world, area.getPointA().getX(), area.getPointA().getY(), area.getPointA().getZ());
		return l;
	}

	public static Location toLocation(World world, Local lobbyLocal) {
		Location l = new Location(world, lobbyLocal.getX(), lobbyLocal.getY(), lobbyLocal.getZ());
		return l;
	}
}