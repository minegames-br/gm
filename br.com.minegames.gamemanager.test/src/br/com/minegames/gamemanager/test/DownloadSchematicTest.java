package br.com.minegames.gamemanager.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import br.com.minegames.core.domain.Arena;
import br.com.minegames.core.domain.Game;
import br.com.minegames.gamemanager.client.GameManagerDelegate;

public class DownloadSchematicTest {
	//public static final String URL_SERVICES = "http://services.minegames.com.br:8080/gamemanager/webresources";
	public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
	
	public static void main(String args[]) throws IOException {
		GameManagerDelegate delegate = GameManagerDelegate.getInstance(URL_SERVICES);
		Game game = delegate.findGame("c6905743-6514-49ba-9257-420743f65b65");
		Arena arena = delegate.findArena("c5253674-8c19-4620-b500-51645a620f64");

		File file = delegate.downloadArenaSchematic(arena.getArena_uuid(), "d:/minecraft/");
		System.out.println(file.getAbsolutePath());
		String lines = FileUtils.readFileToString(file);
		System.out.println(lines);
	}
	

}
