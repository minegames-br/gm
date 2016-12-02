package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;

public class GameQueueJob implements Job {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running GameQueueJob Job.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		List<GameQueue> list = delegate.findAllGameQueueByStatus(GameQueueStatus.WAITING); 

		for(GameQueue gq: list) {
			try {
				//descobrir em qual servidor o jogador está para mandá-lo para o jogo
				MineCraftPlayer mcp = delegate.findPlayerByName(gq.getPlayer().getName());
				ServerInstance server = mcp.getServer();
				if(server == null) {
					//this.cancelGameSubscription(gq);
					throw new Exception("Player info is not synchronized with the server");
				}
				
				//descobrir para qual servidor o jogador vai ser teletransportado para jogar
				List<GameInstance> listGi = delegate.findGameInstanceAvailable(gq.getGame());
				ServerInstance gameServer = null;
				if(listGi != null && listGi.size() > 0) {
					gameServer = listGi.get(0).getServer();
				} else {
					throw new Exception("Nenum GameInstance disponível. Não foi possível adicionar o jogador: " + gq.getPlayer().getName() + " ao jogo: " + gq.getGame().getName() );
				}
				
				AdminClient client = AdminClient.getInstance();
				ResponseDTO rdto = null;
			
				rdto = client.getPlayerInfo( server, gq.getPlayer() );
				if(!rdto.getResult()) {
					this.cancelGameSubscription(gq);
					throw new Exception(rdto.getCode() + " - " + rdto.getMessage());
				}
				
				rdto = client.teleportPlayer(server, gq.getPlayer(), gameServer);
				if(rdto.getResult() ) {
					rdto = client.joinGame( gameServer, gq.getPlayer() );
				} else {
					throw new Exception(rdto.getCode() + " " + rdto.getMessage() );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		System.err.println("Ending UpdateGameInstance Job.");
	}

	private void cancelGameSubscription(GameQueue gq) {
		delegate.removePlayerFromGameQueue(gq);
	}
	
}
