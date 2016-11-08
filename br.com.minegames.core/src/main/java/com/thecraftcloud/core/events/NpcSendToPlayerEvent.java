package com.thecraftcloud.core.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.thecraftcloud.core.npcs.NPC;

public class NpcSendToPlayerEvent extends NPCEvent implements Cancellable {

	protected final Player
			player;
	
	protected boolean
			cancel;
	
	protected String
			name;
	
	protected UUID
			skin;
	
	public NpcSendToPlayerEvent(NPC npc, Player player) {
		super(npc);
		
		this.player = player;
		
		this.name = npc.getName();
		this.skin = npc.getSkin();
		
	}
	
	public final Player getPlayer() {
		
		return player;
	}

	@Override
	public final boolean isCancelled() {

		return cancel;
	}

	@Override
	public final void setCancelled(boolean arg0) {
		cancel = arg0;
		
	}
	
	public final String getName() {
		
		return name;
	}
	
	public final UUID getSkin() {
		
		return skin;
	}

}
