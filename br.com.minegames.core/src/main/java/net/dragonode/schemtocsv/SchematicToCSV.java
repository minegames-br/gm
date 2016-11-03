package net.dragonode.schemtocsv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;

public class SchematicToCSV {

	/**
	 * print the schematic to the file in format:
	 * x,y,z,block,data
	 * @param schematic the schematic file (.schematic)
	 * @param toPrint file to print, should end with .txt
	 */
	public static void print(File schematic, File toPrint) {
		try {
			Writer writer = new FileWriter(toPrint);
			for (String str : lines(schematic)) 
				writer.write(str + "\n");
			
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * get the lines to print from the schematic file
	 * format: x,y,z,block,data
	 * @param schematic the .schematic file
	 * @return lines in the format
	 * @throws IOException
	 */
	public static String[] lines(File schematic) throws IOException {
        FileInputStream fis = new FileInputStream(schematic);
        NBTInputStream nbt = new NBTInputStream(fis);
        CompoundTag backuptag = (CompoundTag) nbt.readTag();
        Map<String, Tag> tagCollection = backuptag.getValue();
        
        List<String> ret = new ArrayList<>();
        short width = (Short)getChildTag(tagCollection, "Width", ShortTag.class).getValue();
        short height = (Short) getChildTag(tagCollection, "Height", ShortTag.class).getValue();
        short length = (Short) getChildTag(tagCollection, "Length", ShortTag.class).getValue();
        
        ret.add("width: " + width);
        ret.add("height: " + height);
        ret.add("length: " + length);
        
		byte[] blockId = (byte[]) getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
        byte[] blockData = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();
        byte[] addId = new byte[0];

        short[] blocks = new short[blockId.length]; // Have to later combine IDs

        for (int index = 0; index < blockId.length; index++) {
            if ((index >> 1) >= addId.length) { // No corresponding AddBlocks index
                blocks[index] = (short) (blockId[index] & 0xFF);
            } else {
                if ((index & 1) == 0) 
                    blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blockId[index] & 0xFF));
                else 
                    blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
            }
            
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    ret.add(x + "," + y + "," + z + "," + blocks[index] + "," + blockData[index] + ";");
                }
            }
        }

        nbt.close();
        fis.close();
        
		return ret.toArray(new String[ret.size()]);

	}

	private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
		Tag tag = items.get(key);
		return tag;
	}

	
	public static void main(String args[]) {
		SchematicToCSV.print(new File("D:/minecraft/worlds/schematics/S2.schematic"), new File("D:/minecraft/worlds/schematics/S2.csv"));
	}
}
