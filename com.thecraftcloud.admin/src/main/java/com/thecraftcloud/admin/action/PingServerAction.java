package com.thecraftcloud.admin.action;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;

public class PingServerAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("I am here up and running!");
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(true);
		return responseDTO;
	}

}
