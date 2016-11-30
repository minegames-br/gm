package com.thecraftcloud.admin.action;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.service.PluginService;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.bungee.BungeeUtils;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.service.ConfigService;

public class JoinGameAction extends Action {

	private ConfigService configService = ConfigService.getInstance();

	@Override
	public ResponseDTO execute(ActionDTO dto) {

		PluginService pService = new PluginService();
		File dir = Bukkit.getWorldContainer();
		
		//Testar se o player veio preenchido
		if(dto.getPlayer() == null) {
			return ResponseDTO.incompleteRequest("Missing Player object.");
		}
		
		//Testar se o minigame esta preparado
		if( configService.getMyCloudCraftGame() == null ) {
			return ResponseDTO.unableToCompleteAction("Game is not yet prepared");
		}
		
		//recuperar o plugin do jogo associado
		TheCraftCloudMiniGameAbstract plugin = (TheCraftCloudMiniGameAbstract)Bukkit.getPluginManager().getPlugin( configService.getGame().getPluginName() );
		
		if( plugin == null ) {
			return ResponseDTO.unableToCompleteAction("Plugin: " + dto.getGame().getPluginName() + " is not installed.");
		}

		BungeeUtils bungeeUtils = new BungeeUtils();
		Player player = Bukkit.getPlayer( dto.getPlayer().getName() );
		plugin.addPlayer( player );
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage( "Player " + dto.getPlayer().getNickName() + " has joined: " +  configService.getGame().getName() );
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(false);
		responseDTO.setCode(ResponseDTO.ADM_SUCCESS);
		
		return responseDTO;
	}

}
