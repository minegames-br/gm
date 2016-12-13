package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;
import com.thecraftcloud.core.json.JSONParser;

public class UpdateGameInstanceJob implements Job {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running UpdateGameInstance Job.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		List<GameInstance> list = delegate.findAllAvailableGameInstance();
		for(GameInstance gi: list) {
			//System.out.println("gi: " + gi.getGame().getName() + " " + gi.getArena().getName() + " " + gi.getServer().getName() + " " + gi.getStatus() + " " + gi.getWorld());
			ServerInstance server = gi.getServer();
			Game game = gi.getGame();

			ActionDTO dto = new ActionDTO();
			dto.setName(ActionDTO.GET_GAME);
		
			try{
				AdminClient client = AdminClient.getInstance();
				ResponseDTO responseDTO = client.execute(server, dto);
				
				if(responseDTO == null) {
					//System.out.println("responseDTO == null. CancelGameInstance");
					cancelGameInstance(gi);
					continue;
				}
				
				if(responseDTO.getResult()) {
					if(responseDTO.getType().equals(ResponseType.JSON)) {
						//System.out.println("response type == JSON.");
						GameInstance _gi = (GameInstance)JSONParser.getInstance().toObject(responseDTO.getJson(), GameInstance.class);
						
						if( gi.getGi_uuid().equals( _gi.getGi_uuid() ) ) {
							//System.out.println("GameInstance ID igual. Atualizando no Banco de Dados.");
							updateGameInstance(_gi);
							continue;
						} else {
							//System.out.println("GameInstance ID diferente. Cancelando Jogo no Banco de Dados.");
							cancelGameInstance(gi);
							continue;
						}
					} else if(responseDTO.getType().equals(ResponseType.TEXT)) {
						//System.out.println("response type == TEXT.");
						//System.out.println("THECRAFTCLOUD \ngame " + gi.getGame().getName() + 
						//		" arena " + gi.getArena().getName() + 
						//		" start " +  (gi.getStartTime() != null?sdf.format(gi.getStartTime().getTime() ):"") + 
						//		" end " + (gi.getEndTime() != null?sdf.format(gi.getEndTime().getTime() ):"") +
						//		" state " + gi.getStatus() ); 

						//System.out.println("MINECRAFT \n" + responseDTO.getMessage());
					}
				} else {
					String s = ( (char)27 + "[34m" + responseDTO.getType() + " " + responseDTO.getMessage() + " " + responseDTO.getResult()  + (char)27 + "[0m"); 
					System.out.println( s );		
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
		String s = ( (char)27 + "[38m" + _gi.getStatus() + " " + _gi.getServer() + " " + _gi.getStartTime() + " " + _gi.getEndTime()  + (char)27 + "[0m"); 
		System.out.println( s );		
		delegate.updateGameInstance(_gi);
	}

	private void cancelGameInstance(GameInstance gi) {
		String s = ( (char)27 + "[40m" + gi.getStatus() + " " + gi.getServer() + " " + gi.getStartTime() + " " + gi.getEndTime()  + (char)27 + "[0m"); 
		System.out.println( s );		

		ServerInstance server = gi.getServer();
		server.setStatus(ServerStatus.ONLINE);
		delegate.updateServer(server);
		delegate.cancelGameInstance(gi);
	}

}
