package com.thecraftcloud.test;

import java.io.InputStream;

import net.minecraft.server.v1_10_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

public class MainSchematic {
		 
    public static void main(String[] args) {
        MainSchematic h = new MainSchematic();
        h.init();
        
    }
    public void init(){
        try {
            
            InputStream fis = getClass().getResourceAsStream("medievalhouse.schematic");
            NBTTagCompound nbtdata = NBTCompressedStreamTools.a(fis);;
 
            
            short width = nbtdata.getShort("Width");
            short height = nbtdata.getShort("Height");
            short length = nbtdata.getShort("Length");
 
            byte[] blocks = nbtdata.getByteArray("Blocks");
            byte[] data = nbtdata.getByteArray("Data");
 

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
		 
}