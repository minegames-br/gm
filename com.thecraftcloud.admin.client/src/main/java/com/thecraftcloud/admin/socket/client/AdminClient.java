package com.thecraftcloud.admin.socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.admin.domain.ResponseDTO;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;

/**
 * This class implements java socket client
 * @author pankaj
 *
 */
public class AdminClient {

    public ResponseDTO execute(ServerInstance server, ActionDTO actionDTO)  {
	    Socket socket = null;

		try {
		    ObjectOutputStream oos = null;
		    ObjectInputStream ois = null;
		
		    //establish socket connection to server
		    socket = new Socket( server.getIp_address(), server.getAdminPort() );
		    
		    String json = JSONParser.getInstance().toJSONString(actionDTO);
		    
		    //write to socket using ObjectOutputStream
		    oos = new ObjectOutputStream(socket.getOutputStream());
		    System.out.println("Sending request to Socket Server");
		    oos.writeObject( json );
		    
		    //read the server response message
		    ois = new ObjectInputStream(socket.getInputStream());
		    String message = (String) ois.readObject();
		    
		    System.out.println("Response: " + message);
		    ResponseDTO responseDTO = (ResponseDTO)JSONParser.getInstance().toObject(message, ResponseDTO.class);
		    //close resources
		    ois.close();
		    oos.close();
		    return responseDTO;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && socket.isConnected() ) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
    }
    
    /**
     *  exemplo de mensagem       
     *  ActionDTO dto = new ActionDTO();
        dto.setName(ActionDTO.GRANT_OPERATOR_ACTION);
        MineCraftPlayer mcp = new MineCraftPlayer();
        mcp.setNickName("_FoxGamer129");
        dto.setPlayer(mcp);

     * 
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static void main2(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        for(int i=0; i<5;i++){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            if(i==4)oos.writeObject("exit");
            else oos.writeObject(""+i);
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
    }
}