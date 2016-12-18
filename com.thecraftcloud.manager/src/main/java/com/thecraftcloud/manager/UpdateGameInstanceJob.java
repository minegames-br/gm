package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;
import com.thecraftcloud.core.json.JSONParser;

public class UpdateGameInstanceJob extends ManagerJob {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running UpdateGameInstance Job.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		List<GameInstance> list = delegate.findAllAvailableGameInstance();
		for(GameInstance gi: list) {
			ServerInstance server = gi.getServer();
			Game game = gi.getGame();

			ActionDTO dto = new ActionDTO();
			dto.setName(ActionDTO.GET_GAME);
		
			try{
				AdminClient client = AdminClient.getInstance();
				ResponseDTO responseDTO = client.execute(server, dto);
				
				if(responseDTO == null) {
					cancelGameInstance(gi);
					continue;
				}
				
				if(responseDTO.getResult()) {
					if(responseDTO.getType().equals(ResponseType.JSON)) {
						GameInstance _gi = (GameInstance)JSONParser.getInstance().toObject(responseDTO.getJson(), GameInstance.class);
						
						if( gi.getGi_uuid().equals( _gi.getGi_uuid() ) ) {
							updateGameInstance(_gi);
							continue;
						} else {
							cancelGameInstance(gi);
							continue;
						}
					}
				} else {
					if(responseDTO.getCode().equals("ADM-999-010")) {
						server.setStatus(ServerStatus.ONLINE);
						delegate.updateServer(server);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("Ending UpdateGameInstance Job.");
	}
	
	private void updateGameInstance(GameInstance _gi) {
		delegate.updateGameInstance(_gi);
	}

	private void cancelGameInstance(GameInstance gi) {
		ServerInstance server = gi.getServer();
		server.setStatus(ServerStatus.ONLINE);
		delegate.updateServer(server);
		delegate.cancelGameInstance(gi);
	}

}
