package com.thecraftcloud.admin.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.bungee.BungeeUtils;

public class SendPlayerToServerAction extends Action {

	public static final String ACTION_NAME = "teleport-player";
	
	@Override
	public ResponseDTO execute(ActionDTO dto) {
		//Testar se o server veio preenchido
		if(dto.getServer() == null) {
			return ResponseDTO.incompleteRequest("Missing Server object.");
		}
		
		TheCraftCloudAdmin plugin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin( "TheCraftCloud-Admin" );
		BungeeUtils bungeeUtils = new BungeeUtils();
		bungeeUtils.setup(plugin);
		Player player = Bukkit.getPlayer( dto.getPlayer().getName() );

		Bukkit.getConsoleSender().sendMessage("&6sending player: " + player.getName() + " to server: " + dto.getServer().getName() );
		bungeeUtils.sendToServer(player, dto.getServer().getName() );
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCode(ResponseDTO.ADM_SUCCESS);
		responseDTO.setMessage("Player: " + player.getName() + " sent to server: " + dto.getServer().getName() );
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(true);
		return responseDTO;
	}

}
