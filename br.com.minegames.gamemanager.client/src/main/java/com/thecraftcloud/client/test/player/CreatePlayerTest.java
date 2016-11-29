package com.thecraftcloud.client.test.player;

import java.util.Calendar;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;

public class CreatePlayerTest {
	public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);

	@Test
	public void test() {
		/*
		MineCraftPlayer player = new MineCraftPlayer();
		player.setEmail("pedroambre@gmail.com");
		player.setName("_WolfGamer");
		player.setNickName("_WolfGamer");
		player.setStatus(PlayerStatus.ONLINE);
		player.setLastLogin(Calendar.getInstance());
		*/

		MineCraftPlayer player = new MineCraftPlayer();
		player.setEmail("joao.emilio@gmail.com");
		player.setName("_KingCraft");
		player.setNickName("_KingCraft");
		player.setStatus(PlayerStatus.ONLINE);
		player.setLastLogin(Calendar.getInstance());

		delegate.createPlayer(player);
		
	}

}
