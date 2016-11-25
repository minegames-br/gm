package com.thecraftcloud.core.bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
 
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
 
public class BungeeUtils {
   
    private static Plugin
            plugin;
   
    public static void setup(Plugin plugin) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        BungeeUtils.plugin = plugin;
       
    }
   
    public static void sendToServer(Player player, String server) {
         send(toByte("Connect", server), player);
    }
   
   
   
    public static byte[] toByte(String...string) {
       
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String str : string)
            out.writeUTF(str);
       
        return out.toByteArray();
    }
   
    public static void send(byte[] data, Player player) {
        player.sendPluginMessage(plugin, "BungeeCord", data);
       
    }
   
}