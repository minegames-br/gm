package com.thecraftcloud.admin.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;

public class GetPlayerInfoAction extends Action {

	public static final String ACTION_NAME = "get-player-info";
	
	@Override
	public ResponseDTO execute(ActionDTO dto) {
		//Testar se o player veio preenchido
		if(dto.getPlayer() == null) {
			return ResponseDTO.incompleteRequest("Missing Player object.");
		}
		
		Player player = Bukkit.getPlayer(dto.getPlayer().getName() );
		if( player == null) {
			return ResponseDTO.unableToCompleteAction("Player is not on this server");
		}
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode(ResponseDTO.ADM_SUCCESS);
		responseDTO.setMessage("Player: " + player.getName() + " is here." );
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(true);
		return responseDTO;
	}

}
