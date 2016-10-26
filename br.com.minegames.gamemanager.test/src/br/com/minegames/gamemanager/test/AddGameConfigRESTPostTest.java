package br.com.minegames.gamemanager.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.minegames.core.domain.Game;
import br.com.minegames.core.domain.GameConfig;
import br.com.minegames.core.domain.GameConfigScope;
import br.com.minegames.core.domain.GameConfigType;
import br.com.minegames.core.json.JSONParser;
import br.com.minegames.gamemanager.client.GameManagerDelegate;

public class AddGameConfigRESTPostTest {

	public static void main(String args[]) {
		String restURL = "http://localhost:8080/gamemanager/webresources";
		//String restURL = "http://jamine-bot.mybluemix.net/webresources";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(restURL);

        GameManagerDelegate delegate = GameManagerDelegate.getInstance(restURL);
        
        /*
        Game game = new Game();
        game.setName("TheLastArcher");
        game.setDescription("The Last Archer is an arcade game. 1-4 players. Goal is to hit targets and not be killed by a Zombie that can explode your base and eat you up.");
        game = delegate.createGame(game);
        */
        Game game = delegate.findGame("4a2bcc03-6ec7-4cbc-805c-00270e370f5d");
        
        /*
        Arena arena = new Arena();
        arena.setName("thelastarcher.pokemongo");
        arena.setDescription("This is the Pokemon Go Arena for The Last Archer.");
        arena = delegate.createArena(arena);
        */
        
        GameConfig domain = new GameConfig();
        domain.setConfigScope(GameConfigScope.GLOBAL);
        domain.setConfigType(GameConfigType.INT);
        domain.setName("thelastarcher.global.countdown");
        domain.setGame(game);
        
        String jsonString;
		jsonString = JSONParser.getInstance().toJSONString(domain);
        Entity<String> entity = Entity.entity(jsonString, MediaType.APPLICATION_JSON);
        Response response = target.path("/game/config/add").request().post(entity);
        System.out.println(response.readEntity(String.class));
	}
}
