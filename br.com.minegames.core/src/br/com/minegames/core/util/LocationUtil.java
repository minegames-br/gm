package br.com.minegames.core.util;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Local;

public class LocationUtil {
	
	public static Location getRandomLocationXZ( World _world, Area3D area ) {

		Local pointA = area.getPointA();
		Local pointB = area.getPointB();
		
		// Como são entidades que não podem voar, definir o spawn no chão
		// a area 2D deve ter Y igual para os pontos A e B 
		int y =  pointA.getY();

		int xSize = pointB.getX() - pointA.getX();
		int zSize = pointB.getZ() - pointA.getZ();
		
		int x = new Random().nextInt(xSize);
		int z = new Random().nextInt(zSize);
		
		z = z + area.getPointA().getZ();
		x = x + area.getPointA().getX();
		
		Location spawnLocation = new Location(_world, x, y, z);
		return spawnLocation;
		
	}
	
	public static Location getRandomLocationXY( World _world, Area3D area ) {

		Local pointA = area.getPointA();
		Local pointB = area.getPointB();
		
		int ySize =  pointB.getY() - pointA.getY();
		int xSize = pointB.getX() - pointA.getX();
		
		//Z não se altera (provavel parede)
		int z = pointB.getZ();
		
		int x = new Random().nextInt(xSize);
		int y = new Random().nextInt(ySize);
		
		y = y + area.getPointA().getY();
		x = x + area.getPointA().getX();
		
		Location spawnLocation = new Location(_world, x, y, z);
		return spawnLocation;
	}
	
	public static Location getRandomLocationXYZ( World _world, Area3D area ) {
		Local pointA = area.getPointA();
		Local pointB = area.getPointB();
		
		int xSize = pointB.getX() - pointA.getX();
		int ySize = pointB.getY() - pointA.getY();
		int zSize = pointB.getZ() - pointA.getZ();
		
		//Z não se altera (provavel parede)
		int x = new Random().nextInt(xSize);
		int y = new Random().nextInt(ySize);
		int z = new Random().nextInt(zSize);
		
		x = x + area.getPointA().getX();
		y = y + area.getPointA().getY();
		z = z + area.getPointA().getZ();
		
		Location spawnLocation = new Location(_world, x, y, z);
		return spawnLocation;
	}

}
