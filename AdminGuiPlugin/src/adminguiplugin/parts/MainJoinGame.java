package adminguiplugin.parts;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.ServerInstance;

public class MainJoinGame {

	private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance("http://services.thecraftcloud.com:8080/gamemanager/webresources");

	public static void main(String[] args) {
		
		String arenaName = "thearcher-stadium";
		String gameName = "TheArcher";
		String playerName = "_WolfGamer";
		
		joinGame(gameName, playerName);
		
		
		
	}
	
	public static void joinGame(String gameName, String playerName) {
		Game game = delegate.findGameByName(gameName);
		MineCraftPlayer player = delegate.findPlayerByName(playerName);
		ActionDTO dto = new ActionDTO();
		dto.setGame(game);
		dto.setName(ActionDTO.JOIN_GAME);
		dto.setPlayer(player);
		ServerInstance server = delegate.findServerByName("localhost");
		
		AdminClient.getInstance().execute(server, dto);
	}

}
