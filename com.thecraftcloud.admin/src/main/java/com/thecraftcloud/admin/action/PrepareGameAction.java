package com.thecraftcloud.admin.action;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.admin.service.GameInstanceService;
import com.thecraftcloud.admin.service.PluginService;
import com.thecraftcloud.admin.service.ServerService;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.GameArenaConfig;
import com.thecraftcloud.core.domain.GameConfigInstance;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.domain.ServerStatus;
import com.thecraftcloud.minigame.TheCraftCloudConfig;
import com.thecraftcloud.minigame.TheCraftCloudMiniGameAbstract;
import com.thecraftcloud.minigame.service.ConfigService;

public class PrepareGameAction extends Action {

	private ConfigService configService = ConfigService.getInstance();
	TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();

	@Override
	public ResponseDTO execute(ActionDTO dto) {

		PluginService pService = new PluginService();
		
		//Testar se o game veio preenchido
		if(dto.getGame() == null) {
			return ResponseDTO.incompleteRequest("Missing Game object.");
		}

		//Testar se a arena veio preenchida
		if(dto.getArena() == null) {
			return ResponseDTO.incompleteRequest("Missing Arena object.");
		}

		//verificar se a arena est� dispon�vel
		World world = Bukkit.getWorld( dto.getArena().getName() );
		if(world == null) {
			world = this.setupGameWorld( dto.getArena() );
			//return ResponseDTO.incompleteRequest("Arena: " + dto.getArena().getName() + " is not available on this server.");
		} else {
			//MultiVerseWrapper mvw = new MultiVerseWrapper();
			//mvw.unloadWorld(world);
		}
		
		//recuperar o plugin do jogo associado
		TheCraftCloudMiniGameAbstract plugin = (TheCraftCloudMiniGameAbstract)Bukkit.getPluginManager().getPlugin( dto.getGame().getPluginName() );
		
		if( plugin == null ) {
			return ResponseDTO.unableToCompleteAction("Plugin: " + dto.getGame().getPluginName() + " is not installed.");
		}
		
		//desabilitar outros plugins TheCraftCloudMiniGameAbstract
		pService.disableTheCraftCloudMiniGames();
		
		//recuperar as configuracoes do jogo
		List<GameArenaConfig> gacList = delegate.findAllGameConfigArenaByGameUUID( dto.getGame().getGame_uuid().toString() );
		List<GameConfigInstance> gciList = delegate.findAllGameConfigInstanceByGameUUID( dto.getGame().getGame_uuid().toString() );

		configService.setMyCloudCraftGame( plugin.createMyCloudCraftGame() );
		configService.setConfig( TheCraftCloudConfig.getInstance() );
		configService.setArena(plugin, dto.getArena());
		configService.setGame(plugin, dto.getGame());
		configService.setGacList(new CopyOnWriteArraySet<>(gacList) );
		configService.setGciList(new CopyOnWriteArraySet<>(gciList));
		configService.setWorld(world);
		
		TheCraftCloudAdmin pluginAdmin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin( TheCraftCloudAdmin.PLUGIN_NAME );

		ServerService sService = new ServerService();
		ServerInstance server = sService.getServerInstance(pluginAdmin.getServerName());
		
		GameInstanceService giService = new GameInstanceService();
		GameInstance gi = new GameInstance();
		gi.setArena(dto.getArena());
		gi.setGame(dto.getGame());
		gi.setStartTime(Calendar.getInstance());
		gi.setServer(server);
		gi = giService.createGameInstance(gi);
		
		//atualizar o server para ele estar no modo esperando pelo jogo
		server.setStatus(ServerStatus.INGAME);
		delegate.updateServer(server);
		
		configService.setGameInstance(gi);
		
		//habilitar o plugin do jogo a ser preparado
		Bukkit.getPluginManager().enablePlugin(plugin);
		
		String s = ( (char)27 + "[32m" + "[PrepareGameAction] GAME: " + dto.getGame().getName() + " ARENA: " + dto.getArena().getName()  + (char)27 + "[0m") + " - plugin.init()"; 
		Bukkit.getConsoleSender().sendMessage(s);
		//System.out.println(s);
		
		//inicializar plugin para receber jogadores
		plugin.init();
		
		s = ( (char)27 + "[32m" + "[PrepareGameAction]"  + (char)27 + "[0m") + " - responseDTO - " + "Game: " + dto.getGame().getName() + " has been prepared to run on Arena: " + dto.getArena().getName() ; 
		Bukkit.getConsoleSender().sendMessage(s);
		//System.out.println(s);
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage( "Game: " + dto.getGame().getName() + " has been prepared to run on Arena: " + dto.getArena().getName() );
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(true);
		responseDTO.setCode(ResponseDTO.ADM_SUCCESS);
		
		return responseDTO;
	}
	
	private World setupGameWorld( Arena arena ) {
		File worldContainerDir = Bukkit.getWorldContainer();

		//recuperar o plugin TheCraftCloudAdmin
		TheCraftCloudAdmin plugin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin( TheCraftCloudAdmin.PLUGIN_NAME );
		
		Bukkit.getServer().unloadWorld(arena.getName(), false);
		//Abrir uma thread para fazer download do mundo
		GameWorld gw = delegate.findGameWorldByName(arena.getName());
		delegate.downloadWorld( gw , worldContainerDir);
		
		//MultiVerseWrapper wrapper = new MultiVerseWrapper();
		return Bukkit.createWorld(new WorldCreator(arena.getName()));
		
	}

}
