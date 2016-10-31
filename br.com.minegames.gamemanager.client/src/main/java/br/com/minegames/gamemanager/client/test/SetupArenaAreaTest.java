package br.com.minegames.gamemanager.client.test;

import java.util.List;

import org.junit.Test;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.GameArenaConfig;
import br.com.minegames.core.domain.Local;
import br.com.minegames.gamemanager.client.GameManagerDelegate;

public class SetupArenaAreaTest {

	
	//public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {
		GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
		
		Arena arena = delegate.findArena("04cdb0ab-bbc2-41b9-8ccb-5cd555838f68");
		
		Area3D area = new Area3D();
		Local l = new Local(494, 4, 1163);
		Local l2 = new Local(456, 21, 1200);
		area.setPointA(l);
		area.setPointB(l2);
		
		area = delegate.addArea3D(area);
		
		arena.setArea(area);
		
		delegate.updateArena(arena);
	}

}
