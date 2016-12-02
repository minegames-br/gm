package com.thecraftcloud.admin.test;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import com.thecraftcloud.admin.socket.client.AdminClient;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;
import com.thecraftcloud.core.domain.Game;
import com.thecraftcloud.core.domain.GameInstance;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;

public class ListOpenGameInstanceTest {

	
	public static final String URL_SERVICES = "http://services.thecraftcloud.com:8080/gamemanager/webresources";
	//public static final String URL_SERVICES = "http://localhost:8080/gamemanager/webresources";
	//String restURL = "";
    private static TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
	
	@Test
	public void test() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance(URL_SERVICES);
		List<GameInstance> list = delegate.findAllAvailableGameInstance();
		for(GameInstance gi: list) {
			ServerInstance server = gi.getServer();
			Game game = gi.getGame();

			ActionDTO dto = new ActionDTO();
			dto.setName(ActionDTO.GET_GAME);
		
			try{
				AdminClient client = AdminClient.getInstance();
				ResponseDTO responseDTO = client.execute(server, dto);
				if(responseDTO.getResult()) {
					if(responseDTO.getType().equals(ResponseType.JSON)) {
						GameInstance _gi = (GameInstance)JSONParser.getInstance().toObject(responseDTO.getJson(), GameInstance.class);
						
						if( gi.getGi_uuid().equals( _gi.getGi_uuid() ) ) {
							System.out.println("GameInstance ID igual");
						} else {
							System.out.println("GameInstance ID igual");
						}
						System.out.println("THECRAFTCLOUD \ngame " + gi.getGame().getName() + 
								" arena " + gi.getArena().getName() + 
								" start " +  sdf.format(gi.getStartTime() ) + 
								" end " + sdf.format(gi.getEndTime()) +
								" state " + gi.getStatus() ); 
						System.out.println("MINECRAFT \ngame " + _gi.getGame().getName() + 
								" arena " + _gi.getArena().getName() + 
								" start " +  sdf.format(_gi.getStartTime() ) + 
								" end " + sdf.format(_gi.getEndTime()) +
								" state " + _gi.getStatus() ); 

					} else if(responseDTO.getType().equals(ResponseType.TEXT)) {
						System.out.println("THECRAFTCLOUD \ngame " + gi.getGame().getName() + 
								" arena " + gi.getArena().getName() + 
								" start " +  gi.getStartTime() != null?sdf.format(gi.getStartTime().getTime() ):"" + 
								" end " + gi.getEndTime() != null?sdf.format(gi.getEndTime().getTime() ):"" +
								" state " + gi.getStatus() ); 

						System.out.println("MINECRAFT \n" + responseDTO.getMessage());
					}
				} else {
					System.out.println("THECRAFTCLOUD \ngame " + gi.getGame().getName() ); 
					System.out.println(" arena " + gi.getArena().getName() + 
							" start " +  (gi.getStartTime() != null?sdf.format(gi.getStartTime().getTime() ):"") + 
							" end " + (gi.getEndTime() != null?sdf.format(gi.getEndTime().getTime() ):"") +
							" state " + gi.getStatus() ); 

					System.out.println("MINECRAFT \n" + responseDTO.getMessage());
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
