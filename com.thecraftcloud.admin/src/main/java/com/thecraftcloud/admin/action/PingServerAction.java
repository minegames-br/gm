package com.thecraftcloud.admin.action;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;

public class PingServerAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("I am here up and running!");
		responseDTO.setResult(true);
		return responseDTO;
	}

}
