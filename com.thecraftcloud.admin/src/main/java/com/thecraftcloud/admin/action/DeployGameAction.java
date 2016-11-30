package com.thecraftcloud.admin.action;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.PluginService;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.minigame.service.ConfigService;

public class DeployGameAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {

		PluginService pService = new PluginService();
		File dir = Bukkit.getWorldContainer();
		
		//Testar se o game veio preenchido
		if(dto.getGame() == null) {
			return ResponseDTO.incompleteRequest("Missing Game object.");
		}

		//Testar se a arena veio preenchida
		if(dto.getArena() == null) {
			return ResponseDTO.incompleteRequest("Missing Arena object.");
		}

		//verificar se a arena está disponível
		World world = Bukkit.getWorld( dto.getArena().getName() );
		if(world == null) {
			return ResponseDTO.incompleteRequest("Arena: " + dto.getArena().getName() + " is not available on this server.");
		}
		
		//recuperar o plugin do jogo associado
		TheCraftCloudAdmin plugin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin( dto.getGame().getPluginName() );
		
		if( plugin == null ) {
			return ResponseDTO.incompleteRequest("Plugin: " + dto.getGame().getPluginName() + " is not installed.");
		}
		
		//desabilitar outros plugins TheCraftCloudMiniGameAbstract
		pService.disableTheCraftCloudMiniGames();
		
		//recuperar as configuracoes do jogo
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		List<GameArenaConfig> gacList = delegate.findAllGameConfigArenaByGameUUID( dto.getGame().getGame_uuid().toString() );
		List<GameConfigInstance> gciList = delegate.findAllGameConfigInstanceByGameUUID( dto.getGame().getGame_uuid().toString() );

		ConfigService.getInstance().setArena(plugin, dto.getArena());
		ConfigService.getInstance().setGame(plugin, dto.getGame());
		ConfigService.getInstance().setGacList( new CopyOnWriteArraySet<>(gacList) );
		ConfigService.getInstance().setGciList(new CopyOnWriteArraySet<>(gciList) );
		
		//habilitar o plugin do jogo a ser preparado
		Bukkit.getPluginManager().enablePlugin(plugin);
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage( "Game: " + dto.getGame().getName() + " has been prepared to run on Arena: " + dto.getArena().getName() );
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(false);
		responseDTO.setCode(ResponseDTO.ADM_SUCCESS);

		
		
		return responseDTO;
	}

}
