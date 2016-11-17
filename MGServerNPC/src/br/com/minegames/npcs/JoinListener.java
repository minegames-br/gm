package br.com.minegames.npcs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import br.com.minegames.main.Main;

public class JoinListener implements Listener {

	private Main game;

	public JoinListener(Main main) {
		super();
		this.game = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		for (NPC npc : NPC.getInstances())
			npc.update(player);
	}
}
