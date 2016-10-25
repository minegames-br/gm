package br.com.minegames.gamemanager.client;

import org.bukkit.plugin.java.JavaPlugin;

public class GameManagerClientPlugin extends JavaPlugin {

	public static final String MINEGAMES_GAMEMANAGER_URL = "minegames.gamemanager.url";
	
	private static String minegamesGameManagerUrl;
	
	@Override
	public void onEnable() {
		minegamesGameManagerUrl = this.getConfig().getString(this.MINEGAMES_GAMEMANAGER_URL);
	}

	public static String getMinegamesGameManagerUrl() {
		return minegamesGameManagerUrl;
	}

	public static void setMinegamesGameManagerUrl(String _minegamesGameManagerUrl) {
		minegamesGameManagerUrl = _minegamesGameManagerUrl;
	}
	
	
	
}
