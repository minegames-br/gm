package com.thecraftcloud.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SandstoneType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.Sandstone;
import org.bukkit.material.Stairs;
import org.bukkit.material.Wool;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.export.ExportBlock;

public class BlockManipulationUtil {
	
    private LocationUtil locationUtil = new LocationUtil();

	public Block createNewWool(World world, double x, double y, double z, DyeColor color) {

    	Bukkit.getLogger().info("x:" + x + " y:" + y + "z: " + z);
    	Location targetLocation = new Location(world, x, y, z);
    	//Bukkit.getConsoleSender().sendMessage(Utils.color("&6Creating New Block " + targetLocation + " - " + world));
        Block block = world.getBlockAt(targetLocation);
    	//Bukkit.getConsoleSender().sendMessage(Utils.color("&6world.getBlockAt" + world.getBlockAt(targetLocation)));
    	
       	block.setType(Material.WOOL);
       	if(color != null) {
       		BlockState state = block.getState();//Grab its generic BlockState
       		
       		Wool woolData = (Wool)state.getData();//Since we just set the block type to wool, we should be fine casting its data to Wool
       		
       		woolData.setColor(color);//Set the new color
       		state.setData(woolData);//Re-apply the new Wool data to the BlockState
       		state.update();//Update the BlockState to finish the changes
       	}
       	return block;
    }
    
    public void createWoolBlocks(Location l1, Location l2, DyeColor color) {
    	List<Block> blocks = blocksFromTwoPoints(l1, l2);
    	for(Block block: blocks) {
       		createNewWool(block.getWorld(), block.getX(), block.getY(), block.getZ(), color);
    	}
    }
    
    public List<Block> blocksFromTwoPoints(World world, Local loc1, Local loc2) {
    	LocationUtil locationUtil = new LocationUtil();
    	Location pointA = locationUtil.toLocation(world, loc1);
    	Location pointB = locationUtil.toLocation(world, loc2);
    	
    	return blocksFromTwoPoints(pointA, pointB);
    }

    public List<Block> blocksFromTwoPoints(Location loc1, Location loc2)
    {
        List<Block> blocks = new ArrayList<Block>();
 
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
 
        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
 
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
 
        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);
                   
                    blocks.add(block);
                }
            }
        }
       
        return blocks;
    }

	public void clearBlocks(Location l1, Location l2) {
    	List<Block> blocks = blocksFromTwoPoints(l1, l2);
    	for(Block block: blocks) {
    		block.setType(Material.AIR);
    	}
	}

	public void destroyArea3D(Player player, Area3D selection) {
		long time = System.currentTimeMillis();
		player.sendMessage("Reading blocks");
		Location pointA = locationUtil .toLocation(player.getWorld(), selection.getPointA());
		Location pointB = locationUtil.toLocation(player.getWorld(), selection.getPointB());
		List<Block> list = blocksFromTwoPoints(pointA, pointB);
		player.sendMessage((System.currentTimeMillis()-time)/1000 + " secs to read " +  list.size() + " blocks");
		time = System.currentTimeMillis();
		
		player.sendMessage("Destroying blocks");
		time = System.currentTimeMillis();
		for(Block block: list ) {
	    	block.setType(Material.AIR);
		    block.getState().update();
		    player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
	    }
		player.sendMessage((System.currentTimeMillis()-time)/1000 + " secs to destroy " +  list.size() + " blocks");
	}

	public void destroyArea3D(World world, Area3D selection) {
		long time = System.currentTimeMillis();
		Location pointA = locationUtil .toLocation(world, selection.getPointA());
		Location pointB = locationUtil.toLocation(world, selection.getPointB());
		List<Block> list = blocksFromTwoPoints(pointA, pointB);
		time = System.currentTimeMillis();
		
		Bukkit.getLogger().info("Destroying blocks");
		time = System.currentTimeMillis();
		for(Block block: list ) {
	    	block.setType(Material.AIR);
		    block.getState().update();
	    }
		Bukkit.getLogger().info((System.currentTimeMillis()-time)/1000 + " secs to destroy " +  list.size() + " blocks");
	}

	public void exportSelection(Player player, Area3D area, File folder) {
		Location pointA = Utils.toLocation(player.getWorld(), area.getPointA());
		Location pointB = Utils.toLocation(player.getWorld(), area.getPointB());
		
		long time = System.currentTimeMillis();
		
		player.sendMessage("Reading blocks");
		List<Block> list = blocksFromTwoPoints(pointA, pointB);
		player.sendMessage((System.currentTimeMillis()-time)/1000 + " secs to read " +  list.size() + " blocks");
		time = System.currentTimeMillis();
		
		List<ExportBlock> blocks = new ArrayList<ExportBlock>();
		
		player.sendMessage("Serializing blocks");
		PrintWriter out;
		try {
			if(!folder.exists()) {
				folder.mkdir();
			}
			File file = new File(folder, "selection.blocks");
			out = new PrintWriter( file );
			out.println(pointA.getBlockX() + "," + pointA.getBlockY() + "," + pointA.getBlockZ() + "," + pointB.getBlockX() + "," + pointB.getBlockY() + "," + pointB.getBlockZ());
			for(Block block: list) {
				if(block.getType() == Material.AIR) {
					continue;
				}
				ExportBlock b = new ExportBlock();
				b.setTypeId(block.getType().getId());
				DyeColor color = null;
				String scolor = "";
				String sandstone = "";
				String facing = "";
				String inverted = "";
				String data = block.getState().getData().getItemType().name();
				int typeId = block.getState().getData().getData();
				if(block.getType() == Material.WOOL) {
		       		BlockState state = block.getState();//Grab its generic BlockState
		       		Wool wool = (Wool)state.getData();//Since we just set the block type to wool, we should be fine casting its data to Wool
					color = wool.getColor();
					scolor = wool.getColor().name();
				} else if( block.getType() == Material.SANDSTONE) {
		       		BlockState state = block.getState();//Grab its generic BlockState
					Sandstone s = (Sandstone)state.getData();
					sandstone = s.getType().name();
				} else if( block.getState().getData() instanceof Directional ) {
					Directional directional = (Directional) block.getState().getData();
					facing = directional.getFacing().name();
				} else if (block.getType() == Material.QUARTZ_STAIRS) {
					Stairs stairs = (Stairs) block.getState().getData();
					if(stairs.isInverted()) {
						inverted = "true";
					}
				}
				
				/*
				if(block.getState() instanceof Wool) {
					Wool w = (Wool)block.getState();
					b.setColor(w.getColor());
				}
				 */
				Location l = block.getLocation();
				out.println("" + l.getBlockX()+","+l.getBlockY() + "," + l.getBlockZ() + "," + l.getYaw() + "," + l.getPitch() + "," + block.getType().name() + "," + scolor + "," + sandstone + "," + facing + "," + inverted + "," + typeId + "," + data);
			}
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		player.sendMessage((System.currentTimeMillis()-time)/1000 + " secs to serialize blocks");
		time = System.currentTimeMillis();
		
		/*String json = JSONParser.getInstance().toJSONString(blocks);
		PrintWriter out;
		try {
			player.sendMessage("Exporting to file");
			out = new PrintWriter("c:/temp/filename.txt");
			player.sendMessage("File exported");
			out.println(json);
			player.sendMessage((System.currentTimeMillis()-time)/1000 + " secs to export file");
			time = System.currentTimeMillis();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void importSelection(final JavaPlugin plugin, File file, final Player player) {
	}
	
	public void createBlock(final JavaPlugin plugin, final World world, final ExportBlock block) {
    	Location l = new Location(world, block.getX(), block.getY(), block.getZ(), block.getYaw(), block.getPitch());
    	Block b = l.getBlock();
    	b.setType(block.getMaterial());
		BlockState state = b.getState();

    	if(block.getMaterial() == Material.WOOL) {
    		Wool wool = (Wool)state.getData();
    		wool.setColor(block.getColor());
    	} else if(block.getMaterial() == Material.SANDSTONE) {
    		Sandstone sandstone = (Sandstone)state.getData();
    		sandstone.setType(block.getSandstoneType());
    	} else if(block.getMaterial() == Material.QUARTZ_STAIRS) {
    		Stairs stairs = (Stairs)state.getData();
    		stairs.setData((byte)0);
    		stairs.setFacingDirection(BlockFace.WEST);
    	}
	
    	//b.getState().update();
	}

	public List<ExportBlock> loadSchematic(JavaPlugin plugin, File file, Player player) {
		return this.loadSchematic(plugin, file, player.getWorld());
	}
	
	public List<ExportBlock> loadSchematic(JavaPlugin plugin, File file, World world) {
		long time = System.currentTimeMillis();
		List<ExportBlock> blocks = new ArrayList<ExportBlock>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    
		    String[] header = line.split(",");
		    Local pointA = new Local();
		    Local pointB = new Local();
		    pointA.setX(Integer.parseInt(header[0]));
		    pointA.setY(Integer.parseInt(header[1]));
		    pointA.setZ(Integer.parseInt(header[2]));
		    pointB.setX(Integer.parseInt(header[3]));
		    pointB.setY(Integer.parseInt(header[4]));
		    pointB.setZ(Integer.parseInt(header[5]));

		    Area3D selection = new Area3D();
		    selection.setPointA(pointA);
		    selection.setPointB(pointB);
		    
		    //destroyArea3D(player, selection);
		    line = br.readLine();

		    while (line != null) {
		    	String[] fields = line.split(",");
		        //Bukkit.getLogger().info(line);
		    	ExportBlock block = new ExportBlock();
		    	block.setX( Integer.parseInt(fields[0]));
		    	block.setY( Integer.parseInt(fields[1]));
		    	block.setZ( Integer.parseInt(fields[2]));
		    	block.setYaw(Float.parseFloat(fields[3]));
		    	block.setPitch(Float.parseFloat(fields[4]));
		    	block.setMaterial(Material.valueOf(fields[5]));

		    	if(block.getMaterial() == Material.WOOL) {
		    		block.setColor(DyeColor.valueOf(fields[6]));
		    	} else if(block.getMaterial() == Material.SANDSTONE) {
		    		block.setSantstoneType(SandstoneType.valueOf(fields[7]));
		    	}
		    	//Bukkit.getLogger().info("fields.length" + fields.length);
		    	if(fields.length >= 9 && !fields[8].trim().equals("")) {
			    	//Bukkit.getLogger().info("fields[8)" + fields[8]);
		    		block.setFace(BlockFace.valueOf(fields[8]));
		    	}
		    	
		    	if(fields.length >= 10 && !fields[9].trim().equals("")) {
		    		block.setInverted(fields[9]);
		    	}
		    	
		    	if(fields.length >= 11 && !fields[10].trim().equals("")) {
		    		block.setTypeId(Integer.parseInt(fields[10]));
		    	}
		    	
		    	if(fields.length >= 12 && !fields[11].trim().equals("")) {
		    		block.setData(fields[11]);
		    	}
		    	
		    	blocks.add(block);
		    	
		    	line = br.readLine();
		    	if(line == null || line.trim().equals("")) {
		    		break;
		    	}
		    	
		    }
			time = System.currentTimeMillis();

		    br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return blocks;
	}
	
	public List<Block> getArenaBlocks(World world, Arena arena) {
		Local pointA = arena.getArea().getPointA();
		Local pointB = arena.getArea().getPointB();
		
		List<Block> list = blocksFromTwoPoints(world, pointA, pointB);
		return list;
	}

	public List<Chest> getArenaChests(World world, Arena arena) {
		List<Chest> chestList = new ArrayList<Chest>();
		List<Block> blocks = getArenaBlocks(world, arena);
		
		for(Block block: blocks) {
			if(block.getType().equals(Material.CHEST)) {
				Chest chest = (Chest)block.getState();
				chestList.add(chest);
			}
		}
		
		return chestList;
	}
}
