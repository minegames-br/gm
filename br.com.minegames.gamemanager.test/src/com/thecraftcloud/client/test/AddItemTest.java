package com.thecraftcloud.client.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.domain.Item;

public class AddItemTest extends TheCraftCloudJUnitTest {
	
	
	@Test
	public void test() {

		try {
			List<String> lines = FileUtils.readLines(new File("c:/temp/material_list.txt"));

			int id = 444;
			for(String line: lines) {
			
				if(line == null || line.trim().equals("")) {
					continue;
				}
				
				if(line.trim().equals("ACACIA_DOOR")) {
					continue;
				}
				
				Item item = new Item();
				item.setId(id++);
				item.setName( line.trim() );
				
				delegate.addItem(item);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
