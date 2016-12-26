package com.thecraftcloud.client.test.kit;

import org.bukkit.Material;
import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.Item;
import com.thecraftcloud.core.domain.Kit;

public class CreateKitTest  extends TheCraftCloudJUnitTest {
	

	@Test
	public void test() {
		Kit kit = null;
		Item item = null;
		kit = delegate.findKit("6e1c9419-233d-4100-af03-ffa46cf46915");
		Game game = delegate.findGameByName("domination");
		addKitToGame( kit, game );
	}
	
	private void addKitToGame( Kit kit, Game game ) {
		game.getKit().add(kit);
		delegate.updateGame(game);
	}
	
	private Kit createKitWarrior() {
		//{"kit_uuid":"6e1c9419-233d-4100-af03-ffa46cf46915","name":"Guerreiro","items":null}
		Kit kit = null;
		Item item = null;
		kit = delegate.findKit("6e1c9419-233d-4100-af03-ffa46cf46915");
		
		item = delegate.findItemByName(Material.DIAMOND_LEGGINGS.name());
		addItemToKit(kit, item);
		item = delegate.findItemByName(Material.DIAMOND_HELMET.name());
		addItemToKit(kit, item);
		item = delegate.findItemByName(Material.DIAMOND_BOOTS.name());
		addItemToKit(kit, item);
		item = delegate.findItemByName(Material.DIAMOND_CHESTPLATE.name());
		addItemToKit(kit, item);
		return delegate.addKit(kit);
	}
	
	private void addItemToKit( Kit kit, Item item ) {
		delegate.addItemToKit(kit, item);
	}
	
	public static void main(String args[]) {
		System.out.println(Material.DIAMOND_CHESTPLATE.name());
	}

}
