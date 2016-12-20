package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameQueue;
import com.thecraftcloud.core.domain.GameQueueStatus;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;

public class GameQueueJob extends ManagerJob {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("Running GameQueueJob Job.");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		logger.info("run lock game queue" );
		delegate.lockGameQueue();
		logger.info("after running lock game queue" );
		
		List<GameQueue> list = delegate.findAllGameQueueByStatus(GameQueueStatus.PROCESSING);
		logger.info("findAllGameQueueByStatus - PROCESSING: " + (list != null?list.size():"") );
		List<GameQueue> list2 = delegate.findAllGameQueueByStatus(GameQueueStatus.WAITING);
		logger.info("findAllGameQueueByStatus - WAITING: " + (list2 != null?list2.size():"") );
		TreeSet<Game> invalidGames = new TreeSet<Game>();
		
		//verificar quais jogos os jogadores querem jogar
		TreeSet<Game> games = new TreeSet<Game>();
		for(GameQueue gq: list) {
			games.add(gq.getGame());
		}

		//preparar os servidores para os jogos
		for(Game game: games) {
			ServerInstance server = findFirstServerAvailable();
			if(server == null) {
				break;
			}
			try {
				prepareGame(server, game);
			} catch (Exception e) {
				invalidGames.add(game);
				e.printStackTrace();
			}
		}
		
		for(GameQueue gq: list) {
			
			if(invalidGames.contains(gq.getGame())) {
				notifyPlayer(gq.getPlayer());
				cancelGameSubscription(gq);
				continue;
			}
			
			if(gq.getPlayer() == null) {
				cancelGameSubscription(gq);
				continue;
			}
			
			try {
				//descobrir em qual servidor o jogador está para mandá-lo para o jogo
				MineCraftPlayer mcp = delegate.findPlayerByName(gq.getPlayer().getName());
				ServerInstance server = mcp.getServer();
				if(server == null) {
					this.cancelGameSubscription(gq);
					continue;
				}
				
				//descobrir para qual servidor o jogador vai ser teletransportado para jogar
				List<GameInstance> listGi = delegate.findGameInstanceAvailable(gq.getGame());
				ServerInstance gameServer = null;
				if(listGi != null && listGi.size() > 0) {
					gameServer = listGi.get(0).getServer();
				} else {
					continue;
				}
				
				AdminClient client = AdminClient.getInstance();
				ResponseDTO rdto = null;
			
				rdto = client.getPlayerInfo( server, gq.getPlayer() );
				if(!rdto.getResult()) {
					this.cancelGameSubscription(gq);
					continue;
				}
				
				rdto = client.teleportPlayer(server, gq.getPlayer(), gameServer);
				boolean success = false;
				if(rdto.getResult() ) {
					//Fazer o player entrar no jogo (addPlayer - livePlayers)
					rdto = client.joinGame( gameServer, gq.getPlayer(), gq.getGame() );
				} else {
					throw new Exception(rdto.getCode() + " " + rdto.getMessage() );
				}
				
				completeGameQueueRequest(gq);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		System.err.println("Ending UpdateGameInstance Job.");
	}

	private void completeGameQueueRequest(GameQueue gq) {
		delegate.completeGameQueueRequest(gq);
	}

	private void notifyPlayer(MineCraftPlayer player) {
		AdminClient client = AdminClient.getInstance();
		try {
			client.notifyPlayer(player, "Esse jogo ainda não está pronto. :-(");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ServerInstance findFirstServerAvailable() {
		List<ServerInstance> servers = delegate.findAllGameServerInstanceOnline();
		if(servers != null && servers.size() > 0) {
			return servers.get(0);
		}
		return null;
	}

	private void cancelGameSubscription(GameQueue gq) {
		delegate.removePlayerFromGameQueue(gq);
	}

	private void prepareGame(ServerInstance server, Game game) throws Exception {
		ActionDTO dto = new ActionDTO();
		dto.setName(ActionDTO.PREPARE_GAME);

		List<Arena> arenas = delegate.findArenasByGame(game);
		
		if(arenas == null || arenas.size() == 0) {
			throw new Exception("there are no arenas ready for this game: " + game.getName() );
		}
		
		String s = ""; 
		int index = arenas.size();
		if( arenas.size() == 1) {
			index = 0;
		} else {
			Random rand = new Random();
			int max = arenas.size();
			int min = 0;
		    index = rand.nextInt((max - min) + 1) + min;
		    if(index >= arenas.size()) index = arenas.size()-1;
		}
		
		Arena arena = arenas.get(index);
		
		dto.setGame(game);
		dto.setArena(arena);
		
		try{
			AdminClient client = AdminClient.getInstance();
			ResponseDTO responseDTO = client.execute(server, dto); 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
