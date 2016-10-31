package br.com.minegames.core.worldedit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;

public class WorldEditWrapper {

	public static boolean loadSchematic(World world,File file,Location originx){

		  WorldEdit worldEdit= WorldEdit.getInstance();
		  Location origin = new Location(world, 0, 0, 0);
		  try (InputStream in=new BufferedInputStream(new FileInputStream(file))){
		    BukkitWorld bukkitWorld=new BukkitWorld(world);
		    ClipboardReader reader=ClipboardFormat.SCHEMATIC.getReader(in);
		    WorldData worldData=bukkitWorld.getWorldData();
		    Clipboard clipboard=reader.read(worldData);
		    ClipboardHolder holder=new ClipboardHolder(clipboard,worldData);
		    EditSession editSession=worldEdit.getEditSessionFactory().getEditSession(bukkitWorld,1000000);
		    editSession.enableQueue();
		    editSession.setFastMode(true);
		    Vector to=new Vector(origin.getBlockX(),origin.getBlockY(),origin.getBlockZ());
		    Operation operation=holder.createPaste(editSession,worldData).to(to).ignoreAirBlocks(false).build();
		    Operations.completeLegacy(operation);
		    editSession.flushQueue();
		    editSession.commit();
		    return true;
		  }
		 catch (  Exception e) {
			 e.printStackTrace();
		  }
		  return false;
		}	
	

}
