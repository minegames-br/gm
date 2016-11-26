package com.thecraftcloud.admin.action;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.domain.GameState;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

public class GetGameAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {
		
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		GameState state = null;
		Game game = null;
		for(Plugin plugin: plugins) {
			if(! (plugin instanceof JavaPlugin) ) {
				continue;
			}
			JavaPlugin javaPlugin = (JavaPlugin)plugin;
			if(javaPlugin instanceof TheCraftCloudMiniGameAbstract) {
				TheCraftCloudMiniGameAbstract miniGame = (TheCraftCloudMiniGameAbstract)javaPlugin;
				state = miniGame.getMyCloudCraftGame().getState();
				game = miniGame.getConfigService().getGame();
			}
		}
		
		ResponseDTO responseDTO = new ResponseDTO();
		if(game != null) { 
			responseDTO.setMessage( game.getName() + ":" + state.name() );
			responseDTO.setResult(true);
		} else {
			responseDTO.setMessage( "TheCraftCloudGame not installed?" );
			responseDTO.setResult(false);
		}
		return responseDTO;
	}

}
