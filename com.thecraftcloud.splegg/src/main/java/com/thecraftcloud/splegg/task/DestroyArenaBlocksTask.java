package com.thecraftcloud.splegg.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;

public class DestroyArenaBlocksTask implements Runnable {

	private TheCraftCloudMiniGameAbstract controller;
	private ConfigService configService = ConfigService.getInstance();

	public DestroyArenaBlocksTask(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}

	@Override
	public void run() {

		MyCloudCraftGame game = configService.getMyCloudCraftGame();
		if (!game.isStarted()) return;

		World world = this.configService.getArenaWorld();
		Area3D area3D = this.controller.getArea();

		Location loc1 = new Location(world, area3D.getPointA().getX(), area3D.getPointA().getY(),
				area3D.getPointA().getZ());
		Location loc2 = new Location(world, area3D.getPointB().getX(), area3D.getPointB().getY(),
				area3D.getPointB().getZ());

		List<Block> blocks = new ArrayList<Block>();

		// Método extraido da classe BlockManipulationUtil
		blocksFromToPoints(blocks, loc1, loc2);

		Block[] array = new Block[blocks.size()];
		for (int i = 0; i < blocks.size(); i++)
			array[i] = blocks.get(i);

		Random r = new Random();

		for (int i = 0; i <= 40; i++) {
			int randomBlock = r.nextInt(blocks.size());
			if (!(array[randomBlock].getType() == Material.AIR)) {
				world.createExplosion(array[randomBlock].getLocation(), 2.0F);
				array[randomBlock].setType(Material.AIR);
				array[randomBlock].getState().update();
			}
		}
	}

	private void blocksFromToPoints(List blocks, Location loc1, Location loc2) {
		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for (int x = bottomBlockX; x <= topBlockX; x++) {
			for (int z = bottomBlockZ; z <= topBlockZ; z++) {
				for (int y = bottomBlockY; y <= topBlockY; y++) {
					Block block = loc1.getWorld().getBlockAt(x, y, z);

					blocks.add(block);
				}
			}
		}
	}
}
