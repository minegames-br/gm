package com.thecraftcloud.plugin.service;

import org.bukkit.Location;
import org.bukkit.World;

import com.thecraftcloud.core.domain.Area3D;
import com.thecraftcloud.core.util.LocationUtil;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;


public class LocalService {
	private TheCraftCloudMiniGameAbstract controller;
	private LocationUtil locationUtil = new LocationUtil();


	public LocalService(TheCraftCloudMiniGameAbstract controller) {
		this.controller = controller;
	}

	public Location getMiddle(World world, Area3D area3d) {
		return locationUtil.getMiddle(world, area3d);
	}
}
