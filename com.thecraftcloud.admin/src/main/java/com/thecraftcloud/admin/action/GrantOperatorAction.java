package com.thecraftcloud.admin.action;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.OpCommand;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;

public class GrantOperatorAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {
		//Bukkit.getConsoleSender().sendMessage("op " + dto.getPlayer().getNickName() );

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "op " + dto.getPlayer().getNickName() );
		
		OpCommand cmd = new OpCommand();
		boolean result = cmd.execute(Bukkit.getConsoleSender(), "op", new String[]{ dto.getPlayer().getNickName() });
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("Operator Granted to " + dto.getPlayer().getNickName() + " result " + result);
		responseDTO.setResult(true);
		return responseDTO;
	}

}
