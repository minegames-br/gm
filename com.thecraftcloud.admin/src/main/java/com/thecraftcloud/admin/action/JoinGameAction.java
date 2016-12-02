package com.thecraftcloud.admin.action;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.PlayerService;
import com.thecraftcloud.admin.service.PluginService;
import com.thecraftcloud.admin.service.ServerService;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.bungee.BungeeUtils;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.service.ConfigService;

public class JoinGameAction extends Action {

	private ConfigService configService = ConfigService.getInstance();
	private TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

	@Override
	public ResponseDTO execute(ActionDTO dto) {

		PluginService pService = new PluginService();
		File dir = Bukkit.getWorldContainer();
		
		//Testar se o player veio preenchido
		if(dto.getPlayer() == null) {
			return ResponseDTO.incompleteRequest("Missing Player object.");
		}
		
		//Testar se o minigame esta preparado
		if( configService.getMyCloudCraftGame() == null ) {
			return ResponseDTO.unableToCompleteAction("Game is not yet prepared");
		}
		
		//recuperar o plugin do jogo associado
		TheCraftCloudMiniGameAbstract plugin = (TheCraftCloudMiniGameAbstract)Bukkit.getPluginManager().getPlugin( configService.getGame().getPluginName() );
		
		if( plugin == null ) {
			return ResponseDTO.unableToCompleteAction("Plugin: " + dto.getGame().getPluginName() + " is not installed.");
		}

		//recuperar o plugin do admin
		TheCraftCloudAdmin admin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin( "TheCraftCloud-Admin" );

		ServerInstance sourceServer = dto.getPlayer().getServer();
		ServerInstance destinationServer = new ServerService().getServerInstance(admin.getServerName());
		Player player = Bukkit.getPlayer( dto.getPlayer().getName() );
		
		BungeeUtils bu = new BungeeUtils();
		bu.setup(admin);
		bu.sendToServer(player, destinationServer.getName());
		
		MineCraftPlayer mcp = delegate.findPlayerByName(player.getName());
		
		//give it 3 seconds before it confirms the player is teleported
		for(int i = 0; i < 3; i++) {
			mcp = delegate.findPlayerByName(player.getName());
			if(mcp.getServer() != null && mcp.getServer().getName().equals(destinationServer.getName()) ) {
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		plugin.addPlayer( player );
		
		PlayerService playerService = new PlayerService(admin);
		playerService.joinGame(mcp);

		//descobrir qual servidor esta esperando jogadores para o jogo em questao
		Game game = dto.getGame();
		
		List<GameInstance> listGi = delegate.findGameInstanceAvailable(game);
		if(listGi == null) {
			return ResponseDTO.unableToCompleteAction("No game is available at this time");
		}
		GameInstance gi = listGi.get(0);
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage( "Player " + dto.getPlayer().getNickName() + " has joined: " +  configService.getGame().getName() );
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(false);
		responseDTO.setCode(ResponseDTO.ADM_SUCCESS);
		
		Bukkit.getConsoleSender().sendMessage("&6player: " + player.getName() + " has joined " + dto.getGame().getName() );
		return responseDTO;
	}

}
