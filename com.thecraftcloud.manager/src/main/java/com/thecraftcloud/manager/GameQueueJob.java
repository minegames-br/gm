package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
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
				System.err.println("There are no servers available.");
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
				String s = ( (char)27 + "[36m" + "gq " + gq + " player: " + gq.getPlayer()  + (char)27 + "[0m"); 
				System.out.println(s);
				
				MineCraftPlayer mcp = delegate.findPlayerByName(gq.getPlayer().getName());
				ServerInstance server = mcp.getServer();
				if(server == null) {
					this.cancelGameSubscription(gq);
					System.err.println("Player info is not synchronized with the server");
					continue;
				}
				
				//descobrir para qual servidor o jogador vai ser teletransportado para jogar
				List<GameInstance> listGi = delegate.findGameInstanceAvailable(gq.getGame());
				ServerInstance gameServer = null;
				if(listGi != null && listGi.size() > 0) {
					gameServer = listGi.get(0).getServer();
				} else {
					System.err.println("Nenum GameInstance disponível. Não foi possível adicionar o jogador: " + gq.getPlayer().getName() + " ao jogo: " + gq.getGame().getName() );
					continue;
				}
				
				AdminClient client = AdminClient.getInstance();
				ResponseDTO rdto = null;
			
				rdto = client.getPlayerInfo( server, gq.getPlayer() );
				if(!rdto.getResult()) {
					this.cancelGameSubscription(gq);
					System.err.println(rdto.getCode() + " - " + rdto.getMessage());
					continue;
				}
				
				rdto = client.teleportPlayer(server, gq.getPlayer(), gameServer);
				boolean success = false;
				if(rdto.getResult() ) {
					s = ( (char)27 + "[32m" + "teleport player worked. executing joinGame..."  + (char)27 + "[0m"); 
					System.out.println(s);
					//Fazer o player entrar no jogo (addPlayer - livePlayers)
					rdto = client.joinGame( gameServer, gq.getPlayer(), gq.getGame() );
					
					s = ( (char)27 + "[32m" + "joinGame..." + rdto.getResult() + " " + rdto.getMessage()  + (char)27 + "[0m"); 
					System.out.println(s);

				} else {
					s = ( (char)27 + "[36m" + "teleport player failed. " + rdto.getMessage()  + (char)27 + "[0m"); 
					System.out.println(s);
					throw new Exception(rdto.getCode() + " " + rdto.getMessage() );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		System.err.println("Ending UpdateGameInstance Job.");
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
		} else {
			System.out.println("Game: " + game.getName() + " arenas: " + arenas.size() );
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
		
		for(Arena arena: arenas) {
			s = ( (char)27 + "[36m" + "Arena: " + arena.getName()  + (char)27 + "[0m"); 
			System.out.println(s);
		}
		
		Arena arena = arenas.get(index);
		s = ( (char)27 + "[36m" + "Arena: " + arena.getName()  + (char)27 + "[0m"); 
		System.out.println(s);
		
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
