package com.thecraftcloud.admin.action;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;

public class TeleportPlayerToLobbyAction extends Action {

	public static final String ACTION_NAME = "teleport-player-to-lobby";
	
	@Override
	public ResponseDTO execute(ActionDTO dto) {
		//Testar se o server veio preenchido
		return ResponseDTO.actionNotAllowed();
	}

}
