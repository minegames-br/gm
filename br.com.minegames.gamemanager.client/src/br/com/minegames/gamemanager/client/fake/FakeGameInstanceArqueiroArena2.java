package br.com.minegames.gamemanager.client.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.minegames.core.domain.Area3D;
import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Config;
import br.com.minegames.core.domain.GameInstance;
import br.com.minegames.core.domain.GameWorld;
import br.com.minegames.core.domain.Local;
import br.com.minegames.core.domain.ServerInstance;
import br.com.minegames.core.json.JSONParser;

public class FakeGameInstanceArqueiroArena2 {

	public static GameInstance createGameInstance() {
		FakeGameInstanceArqueiroArena2 fake = new FakeGameInstanceArqueiroArena2();
		GameInstance domain = fake.init();
		return domain;
	}
	
	public GameInstance init() {
		GameInstance gi = new GameInstance();
		
		createServerInstance(gi);
		createGameWorld(gi);
		createArena(gi);
		createConfigs(gi);
		
		return gi;
	}
	
	private void createServerInstance(GameInstance gi) {
		ServerInstance server = new ServerInstance();
		server.setDescription("servidor do minigame arqueiro");
		server.setHostname("localhost");
		server.setIp_address("127.0.0.1");
		server.setName("mcsa01");
		server.setServer_uuid(UUID.randomUUID());
		gi.setServer(server);
	}
	
	private void createGameWorld(GameInstance gi) {
		GameWorld world = new GameWorld();
		world.setName("arena2");
		world.setDescription("MUNDO DO MINIGAME ARQUEIRO - NETHER");
		world.setWorld_uuid(UUID.randomUUID());
		
		gi.setWorld(world);
	}
	
	private void createArena(GameInstance gi) {
		Arena arena = new Arena();
		
		List<Area3D> areas = new ArrayList<Area3D>();
		arena.setAreas(areas);
		
		arena.setArena_uuid(UUID.randomUUID());
		arena.setName("ARQUEIRO-ARENA-NETHER");
		arena.setDescription("Arena do minigame arqueiro com decoração de fogo/lava");
		Local a1 = new Local(153, 146, 100);
		Local a2 = new Local(112, 166, 143);
		Area3D area = new Area3D(a1, a2);
		area.setArea_uuid(UUID.randomUUID());
		area.setName("ON-FIRE");
		area.setType("ARENA");
		arena.getAreas().add(area);

		a1 = new Local(152, 149, 119);
		a2 = new Local(117, 161, 142);
		area = new Area3D(a1, a2);
		area.setArea_uuid(UUID.randomUUID());
		area.setName("ARQUEIRO-FLOATING-AREA");
		area.setType("AREA");
		arena.getAreas().add(area);

		a1 = new Local(152, 149, 119);
		a2 = new Local(117, 149, 142);
		area = new Area3D(a1, a2);
		area.setArea_uuid(UUID.randomUUID());
		area.setName("ARQUEIRO-MONSTERS-SPAWN-AREA");
		area.setType("MONSTER-SPAWN");
		arena.getAreas().add(area);
		
		createPlayerSpawnAreas(areas);

		gi.setArena(arena);
		
		System.out.println("areas");
		for(Area3D a: areas) {
			System.out.println( "area: " + a.getName() + " " + a.getType() );
		}
	}
	
	private void createPlayerSpawnAreas(List<Area3D> areas) {
		Local a1 = null;
		Local a2 = null;
		Area3D area = null;
				
		int y = 147;
		int z = 100;
		int zEnd = 106;
		
		a1 = new Local(152, y, z);
		a2 = new Local(145, y, zEnd);
		area = new Area3D(a1, a2);
		area.setArea_uuid(UUID.randomUUID());
		area.setName("PLAYER-1");
		area.setType("PLAYER-SPAWN");
		areas.add(area);

		a1 = new Local(141, y, z);
		a2 = new Local(134, y, zEnd);
		area = new Area3D(a1, a2);
		area.setArea_uuid(UUID.randomUUID());
		area.setName("PLAYER-2");
		area.setType("PLAYER-SPAWN");
		areas.add(area);
		
		a1 = new Local(130, y, z);
		a2 = new Local(123, y, zEnd);
		area = new Area3D(a1, a2);
		area.setArea_uuid(UUID.randomUUID());
		area.setName("PLAYER-3");
		area.setType("PLAYER-SPAWN");
		areas.add(area);
		
		a1 = new Local(129, y, z);
		a2 = new Local(122, y, zEnd);
		area = new Area3D(a1, a2);
		area.setArea_uuid(UUID.randomUUID());
		area.setName("PLAYER-4");
		area.setType("PLAYER-SPAWN");
		areas.add(area);
		
	}
	
	private void createConfigs(GameInstance gi) {

		List<Config> configs = new ArrayList<Config>();
		
		Config c = null;
		c = createConfig("ARQUEIRO-START-COUNTDOWN", "O", "15");
		configs.add(c);
		
		c = createConfig("ARQUEIRO-MAX-PLAYERS", "O", "4");
		configs.add(c);
		
		c = createConfig("ARQUEIRO-MIN-PLAYERS", "O", "1");
		configs.add(c);
		
		c = createConfig("ARQUEIRO-MAX-MOVING-TARGET", "O", "3");
		configs.add(c);
		
		c = createConfig("ARQUEIRO-MAX-TARGET", "O", "5");
		configs.add(c);
		
		c = createConfig("ARQUEIRO-MAX-ZOMBIE-SPAWNED-PER-PLAYER", "O", "5");
		configs.add(c);
		
		String jsonStringLocal = "";
		
		Local lobbyLocation = new Local(131, 147, 124);
		jsonStringLocal = JSONParser.getInstance().toJSONString(lobbyLocation);
		c = createConfig("ARQUEIRO-LOBBY-LOCATION", "LOCAL", jsonStringLocal);
		configs.add(c);

		gi.setConfigs(configs);
	}

	public Config createConfig(String name, String type, String value) {
		Config config = new Config();
		config.setConfig_uuid(UUID.randomUUID());
		config.setName(name);
		config.setType(type);
		config.setValue(value);
		
		return config;
	}
	

}
