package com.thecraftcloud.admin.action;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.domain.GameState;
import com.thecraftcloud.minigame.domain.MyCloudCraftGame;
import com.thecraftcloud.minigame.service.ConfigService;

public class GetGameInstanceAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {
		ConfigService configService = ConfigService.getInstance();
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		GameState state = null;
		Game game = null;
		MyCloudCraftGame myCraftCloudGame = null;
		
		if( configService.getConfig() == null ) {
			return ResponseDTO.unableToCompleteAction("TheCraftCloudMiniGame is not configured on this server.");
		}
		
		boolean success = false;
		for(Plugin plugin: plugins) {
			if(! (plugin instanceof JavaPlugin) ) {
				continue;
			}
			JavaPlugin javaPlugin = (JavaPlugin)plugin;
			if(javaPlugin instanceof TheCraftCloudMiniGameAbstract) {
				TheCraftCloudMiniGameAbstract miniGame = (TheCraftCloudMiniGameAbstract)javaPlugin;
				myCraftCloudGame = configService.getMyCloudCraftGame();
				success = true;
			}
		}
		
		ResponseDTO responseDTO = new ResponseDTO();
		if(myCraftCloudGame != null) { 
			String json = JSONParser.getInstance().toJSONString(myCraftCloudGame);
			responseDTO.setMessage("Game: " + configService.getGame() );
			responseDTO.setJson( json );
			responseDTO.setType(ResponseType.JSON);
			responseDTO.setResult(true);
		} else {
			responseDTO.setMessage( "TheCraftCloudMiniGame not installed or not prepared?" );
			responseDTO.setType(ResponseType.TEXT);
			responseDTO.setResult(false);
		}

		if(success) {
			responseDTO.setCode(ResponseDTO.ADM_SUCCESS);
		} else {
			responseDTO.setCode(ResponseDTO.ADM_FAILURE);
		}
		return responseDTO;
	}

}
