package com.thecraftcloud.core.bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
 
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
 
public class BungeeUtils {
   
    private Plugin plugin;
    private static BungeeUtils me;
   
    private BungeeUtils() {
    	
    }
    
    public static BungeeUtils getInstance() {
    	if (me == null) {
    		me = new BungeeUtils();
    	}
    	return me;
    }
    
    public void setup(Plugin plugin) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        this.plugin = plugin;
    }
   
    public void sendToServer(Player player, String server) {
         send(toByte("Connect", server), player);
    }
   
   
   
    public byte[] toByte(String...string) {
       
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String str : string)
            out.writeUTF(str);
       
        return out.toByteArray();
    }
   
    public void send(byte[] data, Player player) {
        player.sendPluginMessage(plugin, "BungeeCord", data);
    }
   
}