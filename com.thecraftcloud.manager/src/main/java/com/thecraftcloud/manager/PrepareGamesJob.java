package com.thecraftcloud.manager;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerType;

public class PrepareGamesJob implements Job {
	
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running PrepareGamesJob Job.");

		List<ServerInstance> listServers = delegate.findAllServerInstanceOnline();
		
		for(ServerInstance server: listServers) {
			
			if(server.getType() == null ) {
				continue;
			}
			
			if(!server.getType().equals(ServerType.GAME) ) {
				continue;
			}

			//o status online significa que nao esta em modo jogo, ou seja, disponivel.
			//preparando jogo para o servidor que como tem um filtro acima eh um server
			//do tipo GAME
			//prepareGunGame(server);
		}

			
		System.err.println("Ending PrepareGamesJob Job.");
	}

	private void prepareGunGame(ServerInstance server) {
		ActionDTO dto = new ActionDTO();
		dto.setName(ActionDTO.PREPARE_GAME);
		
		Game game = delegate.findGame("d10e8c62-6124-4952-a054-c7c668e7944f");
		Arena arena = delegate.findArena("30d00221-b371-4828-a0e6-5d75de7bfaec");
		
		dto.setGame(game);
		dto.setArena(arena);
		
		try{
			AdminClient client = AdminClient.getInstance();
			ResponseDTO responseDTO = client.execute(server, dto); 
			System.out.println( server.getName() + " - " + responseDTO.getMessage() + " " + responseDTO.getResult() );
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
