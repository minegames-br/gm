package com.thecraftcloud.admin.action;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;

public abstract class Action {

	public abstract ResponseDTO execute(ActionDTO dto);
	
}
