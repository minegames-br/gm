package net.dragonode.npcs.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.dragonode.npcs.NPC;

public class NPCEvent extends Event {

	@Override
	public HandlerList getHandlers() {

		return new HandlerList();
	}

	public static HandlerList getHandlerList() {
		
		return new HandlerList();
	}
	
	protected final NPC
			npc;
	
	public NPCEvent(NPC npc) {
		this.npc = npc;
		
	}
	
	public final NPC getNPC() {
		
		return npc;
	}
	
}
