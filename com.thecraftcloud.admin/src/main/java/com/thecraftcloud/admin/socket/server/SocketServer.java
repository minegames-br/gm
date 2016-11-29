package com.thecraftcloud.admin.socket.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.thecraftcloud.admin.action.Action;
import com.thecraftcloud.admin.action.ActionFactory;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.json.JSONParser;

/**
 * Socket Server for The Craft Cloud Admin
 * Managing Minecraft servers using JSON messages
 * @author joaoemilio@gmail.com
 *
 */
public class SocketServer {
    
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 65000;
    
    public SocketServer(Integer socketServerPort) {
    	if(socketServerPort != null && socketServerPort < 1000 ) {
    		this.port = socketServerPort;
    	}
	}

	public void start() throws IOException, ClassNotFoundException{
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
            System.out.println("Waiting for client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String json = (String) ois.readObject();
            System.out.println("Message Received: " + json);
            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
            
            try{
            	json = reply(json);
            }catch(Exception e) {
            	e.printStackTrace();
            	json = createExceptionResponse(e);
            }
            
            oos.writeObject(json);
            //close resources
            ois.close();
            oos.close();
            socket.close();
        }
    }
    
    private String createExceptionResponse(Exception e) {
    	ResponseDTO responseDTO = ResponseDTO.unableToCompleteAction("The action generated an exception: " + e.getMessage() );
    	String response = JSONParser.getInstance().toJSONString(responseDTO);

		return response;
	}

	public String reply( String json ) {
    	String response;
    	
    	ActionDTO actionDTO = (ActionDTO)JSONParser.getInstance().toObject(json, ActionDTO.class);
    	
    	if(actionDTO == null) {
    		response = JSONParser.getInstance().toJSONString( ResponseDTO.invalidAction() );
    		return response;
    	}
    	
    	Action action = ActionFactory.getInstance().createAction(actionDTO.getName());

    	ResponseDTO responseDTO;
    	if( action != null) {
    		responseDTO = action.execute(actionDTO);
    	} else { 
			responseDTO = ResponseDTO.actionNotAllowed();
    	}
    	
    	/*
    	if(actionDTO.getName().equals(ActionDTO.GRANT_OPERATOR_ACTION)) {
    		GrantOperatorAction action = new GrantOperatorAction();
    		if( actionDTO.getPlayer() == null || actionDTO.getPlayer().getNickName() == null || actionDTO.getPlayer().getNickName().equals("")) {
    			responseDTO.setMessage("MineCraftPlayer object is missing or NickName is empty/null.");
    			responseDTO.setResult(false);
    		}
    		responseDTO = action.execute(actionDTO);
    	} else if(actionDTO.getName().equals(ActionDTO.PING_SERVER)) {
    		PingServerAction action = new PingServerAction();
    		responseDTO = action.execute(actionDTO);
    	} else if(actionDTO.getName().equals(ActionDTO.GET_GAME)) {
    		GetGameInstanceAction action = new GetGameInstanceAction();
    		responseDTO = action.execute(actionDTO);
    	} else {
			responseDTO = ResponseDTO.actionNotAllowed();
    	}
    	*/
    	
    	response = JSONParser.getInstance().toJSONString(responseDTO);
    	
    	return response;
    }
    
    public void stop() {
        //close the ServerSocket object
        try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}