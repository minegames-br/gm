package adminguiplugin.parts;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameWorld;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;

public class MainPrepareGame {

	private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");

	public static void main(String[] args) {
		
		String arenaName = "thearcher-stadium";
		String gameName = "TheArcher";
		
		prepareGame(gameName, arenaName);
		
		
		
	}
	
	public static void prepareGame(String gameName, String arenaName) {
		ServerInstance server = delegate.findServerByName("localhost-joao");
		System.out.println(server.getHostname() + " " + server.getIp_address() + "  "  + server.getAdminPort());
		Game game = delegate.findGameByName(gameName);
		Arena arena = delegate.findArenaByName(arenaName);
		GameWorld gameWorld = delegate.findGameWorldByName( arenaName );
		ActionDTO dto = new ActionDTO();
		dto.setArena(arena);
		dto.setGame(game);
		dto.setServer(server);
		dto.setGameWorld(gameWorld);
		dto.setName(ActionDTO.PREPARE_GAME);
		AdminClient.getInstance().execute(server, dto);
	}
	
}
