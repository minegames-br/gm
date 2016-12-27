package com.thecraftcloud.admin.socket.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;

import com.thecraftcloud.admin.action.Action;
import com.thecraftcloud.admin.action.ActionFactory;
import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.json.JSONParser;
import com.thecraftcloud.core.util.Utils;

/**
 * Socket Server for The Craft Cloud Admin
 * Managing Minecraft servers using JSON messages
 * @author joaoemilio@gmail.com
 *
 */
public class SocketServer {
    
    //static ServerSocket variable
    private ServerSocket server;
    //socket server port on which it will listen
    private int port = 65000;
    
    public SocketServer(Integer socketServerPort) {
    	Bukkit.getConsoleSender().sendMessage(Utils.color("&8adminPort: " + socketServerPort));
    	if(socketServerPort != null && socketServerPort > 1000 ) {
    		this.port = socketServerPort;
    	}
	}

	public void start() throws IOException, ClassNotFoundException{
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
           // System.out.println("Waiting for client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String json = (String) ois.readObject();
            //System.out.println("Message Received: " + json);
            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
            
            try{
            	System.out.println("request recebido: " + json);
            	json = reply(json);
            	System.out.println("response a ser enviado: " + json);
            }catch(Exception e) {
            	System.out.println("exception processando request: " + e.getMessage() );
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
    	System.out.println("reply");
    	
    	ActionDTO actionDTO = (ActionDTO)JSONParser.getInstance().toObject(json, ActionDTO.class);
    	System.out.println("reply 2");
    	
    	if(actionDTO == null) {
    		response = JSONParser.getInstance().toJSONString( ResponseDTO.invalidAction() );
    		return response;
    	}
    	System.out.println("reply 3");
    	
    	Action action = ActionFactory.getInstance().createAction(actionDTO.getName());
    	System.out.println("reply 4");

    	ResponseDTO responseDTO;
    	if( action != null) {
        	System.out.println("reply 4.1: " + actionDTO.getName() + " " +action.getClass().getName() );
    		responseDTO = action.execute(actionDTO);
        	System.out.println("reply 4.2");
    	} else { 
			responseDTO = ResponseDTO.actionNotAllowed();
    	}
    	System.out.println("reply 5");
    	
    	response = JSONParser.getInstance().toJSONString(responseDTO);
    	System.out.println("reply 6");
    	
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