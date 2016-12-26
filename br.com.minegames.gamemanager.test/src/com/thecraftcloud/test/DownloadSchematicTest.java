package com.thecraftcloud.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.client.exception.InvalidRegistrationException;
import com.thecraftcloud.client.test.TheCraftCloudJUnitTest;
import com.thecraftcloud.core.domain.Arena;
import com.thecraftcloud.core.domain.Game;

public class DownloadSchematicTest extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() throws IOException, InvalidRegistrationException {

		Game game = delegate.findGame("c6905743-6514-49ba-9257-420743f65b65");
		Arena arena = delegate.findArena("c5253674-8c19-4620-b500-51645a620f64");

		File file = delegate.downloadArenaSchematic(arena.getArena_uuid(), "d:/minecraft/");
		System.out.println(file.getAbsolutePath());
		String lines = FileUtils.readFileToString(file);
		System.out.println(lines);
	}
	

}
