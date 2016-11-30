package com.thecraftcloud.admin.action;

import java.io.File;

import org.bukkit.Bukkit;

import com.thecraftcloud.admin.TheCraftCloudAdmin;
import com.thecraftcloud.client.TheCraftCloudDelegate;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.admin.domain.ResponseType;

public class DownloadWorldAction extends Action {

	@Override
	public ResponseDTO execute(ActionDTO dto) {

		File dir = Bukkit.getWorldContainer();
		
		//Testar se o mundo veio preenchido
		if(dto.getGameWorld() == null) {
			return ResponseDTO.incompleteRequest("Missing GameWorld object.");
		}

		//recuperar o plugin TheCraftCloudAdmin
		TheCraftCloudAdmin plugin = (TheCraftCloudAdmin)Bukkit.getPluginManager().getPlugin("TheCraftCloudAdmin");
		
		//Abrir uma thread para fazer download do mundo
		TheCraftCloudDelegate delegate = TheCraftCloudDelegate.getInstance();
		delegate.downloadWorld(dto.getGameWorld(), dir);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage( "World has been downloaded to: " + dir.getAbsolutePath() + "/" + dto.getGameWorld().getName() );
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(false);
		responseDTO.setCode(ResponseDTO.ADM_SUCCESS);

		return responseDTO;
	}

}
