package br.com.minegames.core.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.json.JSONParser;

public class LocationUtil {
	
	public static Location getRandomLocationXYZ( World _world, Area3D area ) {
		Local pointA = area.getPointA();
		Local pointB = area.getPointB();
		
		Bukkit.getConsoleSender().sendMessage(JSONParser.getInstance().toJSONString(pointA));
		Bukkit.getConsoleSender().sendMessage(JSONParser.getInstance().toJSONString(pointB));
		
		int xSize =  Math.abs(pointB.getX() - ( pointA.getX() ) );
		int ySize =  Math.abs(pointB.getY() - ( pointA.getY() ) );
		int zSize =  Math.abs(pointB.getZ() - ( pointA.getZ() ) );
		Bukkit.getConsoleSender().sendMessage("xsize: " + xSize);
		Bukkit.getConsoleSender().sendMessage("ysize:" + ySize);
		Bukkit.getConsoleSender().sendMessage("zsize:" + zSize);
		
		int x = area.getPointA().getX();
		if(xSize > 0) {
			x = new Random().nextInt(xSize);
		}else{
			x = 0;
		}
		int y = area.getPointA().getY();
		if(ySize > 0) {
			y = new Random().nextInt(ySize);
		}else{
			y = 0;
		}
		int z = area.getPointA().getZ();
		if(zSize > 0) {
			z = new Random().nextInt(zSize);
		} else {
			z = 0;
		}
		
		if( area.getPointA().getX() < area.getPointB().getX()) {
			x = x + area.getPointA().getX();
		} else {
			x = area.getPointB().getX() + x;
		}
		if( area.getPointA().getY() < area.getPointB().getY()) {
			y = y + area.getPointA().getY();
		} else {
			y = area.getPointB().getY() + y;
		}
		
		if( area.getPointA().getZ() < area.getPointB().getZ()) {
			z = z + area.getPointA().getZ();
		} else {
			z = area.getPointB().getZ() + z;
		}
		
		Bukkit.getConsoleSender().sendMessage("x: " + x + " - y: " + y + " z: " + z);
		
		Location spawnLocation = new Location(_world, x, y, z);
		return spawnLocation;
	}
	
	public static void main(String args[]) {
		System.out.println(Math.abs(3-(-3)));
		System.out.println(Math.abs(-9-(-3)));
		System.out.println(Math.abs(+9-(-3)));
		System.out.println(Math.abs(+9-(3)));
	}
}
