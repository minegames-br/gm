package com.thecraftcloud.admin.action;

import org.bukkit.Bukkit;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;

public class ExecuteCommandAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {

		Bukkit.getConsoleSender().sendMessage("op " + dto.getPlayer().getNickName() );
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("Operator Granted to " + dto.getPlayer().getNickName() );
		responseDTO.setResult(true);
		return responseDTO;
	}

}
