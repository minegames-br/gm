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
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.core.domain.ServerInstance;

public class GameQueueJob implements Job {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running GameQueueJob Job.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		List<GameQueue> list = delegate.findAllGameQueueByStatus(GameQueueStatus.WAITING); 

		for(GameQueue gq: list) {
			//descobrir qual servidor esta esperando jogadores para o jogo em questao
			Game game = gq.getGame();
			
			List<GameInstance> listGi = delegate.findGameInstanceAvailable(game);
			if(listGi == null) {
				continue;
			}
			GameInstance gi = listGi.get(0);
			ServerInstance server = gi.getServer();
			
			ActionDTO dto = new ActionDTO();
			dto.setName(ActionDTO.JOIN_GAME);
			dto.setPlayer(gq.getPlayer());

			try{
				AdminClient client = new AdminClient();
				ResponseDTO responseDTO = client.execute(server, dto);
				
				if(responseDTO == null) {
					System.err.print("Não foi possível adicionar o jogador: " + gq.getPlayer().getName() + " ao jogo: " + game.getName() );
					continue;
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
		delegate.cancelGameInstance(gi);
	}

}
