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
import com.thecraftcloud.domain.GameState;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class GetGameInstanceAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {
		
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		GameState state = null;
		Game game = null;
		GameInstance gi = null;
		boolean success = false;
		for(Plugin plugin: plugins) {
			if(! (plugin instanceof JavaPlugin) ) {
				continue;
			}
			JavaPlugin javaPlugin = (JavaPlugin)plugin;
			if(javaPlugin instanceof TheCraftCloudMiniGameAbstract) {
				TheCraftCloudMiniGameAbstract miniGame = (TheCraftCloudMiniGameAbstract)javaPlugin;
				state = miniGame.getMyCloudCraftGame().getState();
				game = miniGame.getConfigService().getGame();
				gi = miniGame.getGameInstance();
				success = true;
			}
		}
		
		ResponseDTO responseDTO = new ResponseDTO();
		if(gi == null) {
			responseDTO.setMessage( "GameInstance is null" );
			responseDTO.setType(ResponseType.TEXT);
			responseDTO.setResult(false);
		} else {
		
			if(game != null) { 
				String json = JSONParser.getInstance().toJSONString(gi);
				responseDTO.setMessage("GameInstance: " + gi.getGi_uuid().toString() );
				responseDTO.setJson( json );
				responseDTO.setType(ResponseType.JSON);
				responseDTO.setResult(true);
			} else {
				responseDTO.setMessage( "TheCraftCloudGame not installed?" );
				responseDTO.setType(ResponseType.TEXT);
				responseDTO.setResult(false);
			}
			
		}
		if(success) {
			responseDTO.setCode(ResponseDTO.ADM_SUCCESS);
		} else {
			responseDTO.setCode(ResponseDTO.ADM_FAILURE);
		}
		return responseDTO;
	}

}
