package com.thecraftcloud.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.AdminQueue;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.RequestStatus;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;

public class SendPlayersToLobbyJob implements Job {

	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");
	private Logger logger = Logger.getLogger("file");
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info((char)27 + "[32m" + "Running SendPlayersToLobbyJob Job."+ "[0m");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		List<MineCraftPlayer> list = delegate.findPlayersNotInLobby();
		logger.info((char)27 + "[32m" + "size: " + list.size() + (char)27 + "[0m");
		ServerInstance lobbyServer = delegate.findLobbyAvailable();
		
		for(MineCraftPlayer player: list) {
			logger.info((char)27 + "[32m" + player.getName() + " " + player.getServer().getName() + player.getServer().getStatus() + " " + player.getStatus() + (char)27 + "[0m");

			ActionDTO action = new ActionDTO();
			action.setName(ActionDTO.TELEPORT_PLAYER);
			action.setPlayer(player);
			
			action.setServer(lobbyServer);
			AdminClient client = AdminClient.getInstance();
			ServerInstance server = delegate.findServerByName("mgbungee");
			ResponseDTO rdto = client.execute(server, action);
			if( !rdto.getResult() ) {
				player.setStatus(PlayerStatus.OFFLINE);
				delegate.updatePlayer(player);
			} else {
				player.setStatus(PlayerStatus.ONLINE);
				player.setServer(lobbyServer);
				delegate.updatePlayer(player);
			}
		}
		logger.info((char)27 + "[32m" + "Completing SendPlayersToLobbyJob Job." + (char)27 + "[0m");
	}

}
