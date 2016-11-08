package com.thecraftcloud.core.world.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidWorld extends JavaPlugin {

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidWorldGenerator();
    }

    public class VoidWorldGenerator extends ChunkGenerator {

        public List<BlockPopulator> getDefaultPopulators(World world) {
            return Arrays.asList(new BlockPopulator[0]);
        }

        public boolean canSpawn(World world, int x, int z) {
            return true;
        }

        public byte[] generate(World world, Random rand, int chunkx, int chunkz) {
            return new byte[32768];
        }

        public Location getFixedSpawnLocation(World world, Random random) {
            return new Location(world, 0, 128, 0);
        }

    }

}