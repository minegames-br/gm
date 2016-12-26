package com.thecraftcloud.client.test.player;

import java.util.Calendar;

import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;

public class CreatePlayerTest  extends TheCraftCloudJUnitTest {
	

	@Test
	public void test() {
		/*
		MineCraftPlayer player = new MineCraftPlayer();
		player.setEmail("pedroambre@gmail.com");
		player.setName("_WolfGamer");
		player.setNickName("_WolfGamer");
		player.setStatus(PlayerStatus.ONLINE);
		player.setLastLogin(Calendar.getInstance());

		MineCraftPlayer player = new MineCraftPlayer();
		player.setEmail("joao.emilio@gmail.com");
		player.setName("_KingCraft");
		player.setNickName("_KingCraft");
		player.setStatus(PlayerStatus.ONLINE);
		player.setLastLogin(Calendar.getInstance());
		*/
		MineCraftPlayer player = new MineCraftPlayer();
		player.setEmail("joaoemilio@gmail.com");
		player.setName("FoxGamer129");
		player.setNickName("FoxGamer129");
		player.setStatus(PlayerStatus.ONLINE);
		player.setLastLogin(Calendar.getInstance());

		delegate.createPlayer(player);
		
	}

}
