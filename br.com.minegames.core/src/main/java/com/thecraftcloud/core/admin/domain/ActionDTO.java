package com.thecraftcloud.core.admin.domain;

import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.TransferObject;

public class ActionDTO extends TransferObject {

	public static final String PING_SERVER = "ping-server";
	public static final String GRANT_OPERATOR_ACTION = "grant-operator";
	public static final String EXECUTE_COMMAND = "execute-command";
	public static final String GET_GAME = "get-game";
	
	private String name;
	
	private MineCraftPlayer player;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MineCraftPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MineCraftPlayer player) {
		this.player = player;
	}
	
}
