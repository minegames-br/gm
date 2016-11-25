package com.thecraftcloud.gungame.gyurix;
/**

import gyurix.spigotlib.SU;
import gyurix.spigotutils.LocationData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import static com.thecraftcloud.gungame.GunGameConfig.games;
import static com.thecraftcloud.gungame.GameController.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

 * Created by GyuriX on 2016. 09. 22..
public class CommandGG implements CommandExecutor, TabCompleter {
    public static String[] cmds = {"help", "create", "remove", "info", "setspawn", "leave", "list", "removespawn"};
    public static String[] cmdsConsole = {"help", "create", "remove", "info", "list", "removespawn"};
    
    private GameController controller;

    public CommandGG(GameController controller) {
        PluginCommand pc = GameController.pl.getCommand("gg");
        this.controller = controller;
        pc.setExecutor(this);
        pc.setTabCompleter(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        try {

            String sub = args.length == 0 ? "help" : args[0].toLowerCase();
            if (!sender.hasPermission("gg.command." + sub)) {
                lang.msg(sender, "noperm");
                return true;
            }
            Player plr = sender instanceof Player ? (Player) sender : null;
            switch (sub) {
                case "help":
                    lang.msg("", sender, "help");
                    return true;
                case "create":
                    if (args.length == 1) {
                        lang.msg(sender, "nogame");
                        return true;
                    }
                    if (games.containsKey(args[1])) {
                        lang.msg(sender, "exists", "game", args[1]);
                        return true;
                    }
                    games.put(args[1], new Game(args[1]));
                    lang.msg(sender, "create", "game", args[1]);
                    kf.save();
                    return true;
                case "remove": {
                    if (args.length == 1) {
                        lang.msg(sender, "nogame");
                        return true;
                    }
                    Game g = games.remove(args[1]);
                    if (g == null) {
                        lang.msg(sender, "notexist", "game", args[1]);
                        return true;
                    }
                    g.kill();
                    SU.sch.cancelTask(g.rid);
                    lang.msg(sender, "remove", "game", g.name);
                    kf.save();
                    return true;
                }
                case "info": {
                    if (args.length == 1) {
                        lang.msg(sender, "nogame");
                        return true;
                    }
                    Game g = games.get(args[1]);
                    if (g == null) {
                        lang.msg(sender, "notexist", "game", args[1]);
                        return true;
                    }
                    lang.msg(sender, "info", "game", g.name, "count", g.count, "ingame", g.levels.size(), "state", lang.get(plr, "state." + g.state),
                            "spawns", g.spawns.size() == 0 ? "" : "\n - " + StringUtils.join(g.spawns, "\n - "));
                    return true;
                }
                case "list":
                    lang.msg(sender, "list", "games", StringUtils.join(games.keySet(), ", "));
                    return true;
                case "leave": {
                    Game g = playerGames.remove(plr.getName());
                    if (g == null) {
                        lang.msg(sender, "leave.notin");
                        return true;
                    }
                    g.quit(plr);
                    lang.msg(sender, "leave", "game", g.name);
                    return true;
                }
                case "setspawn": {
                    if (plr == null) {
                        lang.msg(sender, "noconsole");
                        return true;
                    }
                    if (args.length == 1) {
                        lang.msg(sender, "nogame");
                        return true;
                    }
                    if (args.length == 2) {
                        lang.msg(sender, "nospawn");
                        return true;
                    }
                    Game g = games.get(args[1]);
                    if (g == null) {
                        lang.msg(sender, "notexist", "game", args[1]);
                        return true;
                    }
                    int spawnId = -1;
                    try {
                        spawnId = Integer.valueOf(args[2]) - 1;
                    } catch (Throwable e) {
                    }
                    if (spawnId < 0 || spawnId > g.spawns.size()) {
                        lang.msg(sender, "spawn.wrong", "max", g.spawns.size() + 1);
                        return true;
                    }
                    LocationData ld = new LocationData(plr.getLocation().getBlock()).add(0.5, 0, 0.5);
                    ld.yaw = plr.getLocation().getYaw();
                    ld.pitch = plr.getLocation().getPitch();
                    if (spawnId == g.spawns.size())
                        g.spawns.add(ld);
                    else
                        g.spawns.set(spawnId, ld);
                    lang.msg(sender, "spawn.set", "id", spawnId + 1, "game", g.name, "value", ld);
                    kf.save();
                    return true;
                }

                case "removespawn":
                    if (args.length == 1) {
                        lang.msg(sender, "nogame");
                        return true;
                    }
                    Game g = games.get(args[1]);
                    if (g == null) {
                        lang.msg(sender, "notexist", "game", args[1]);
                        return true;
                    }
                    if (g.spawns.isEmpty()) {
                        lang.msg(sender, "spawn.none", "game", g.name);
                        return true;
                    }
                    if (args.length == 2) {
                        g.spawns.clear();
                        lang.msg(sender, "spawn.removeall", "game", args[1]);
                        kf.save();
                        return true;
                    }
                    int spawnId = -1;
                    try {
                        spawnId = Integer.valueOf(args[2]) - 1;
                    } catch (Throwable e) {
                    }
                    if (spawnId < 0 || spawnId >= g.spawns.size()) {
                        lang.msg(sender, "spawn.wrong", "max", g.spawns.size());
                        return true;
                    }
                    g.spawns.remove(spawnId);
                    lang.msg(sender, "spawn.remove", "id", spawnId + 1, "game", g.name);
                    kf.save();
                    return true;
            }
        } catch (Throwable e) {
            SU.error(sender.hasPermission("gg.debug") ? sender : SU.cs, e, "GunGame", "gyurix");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        TreeSet<String> list = new TreeSet<>();
        args[0] = args[0].toLowerCase();
        if (args.length == 1) {
            for (String c : sender instanceof Player ? cmds : cmdsConsole) {
                if (c.startsWith(args[0]) && sender.hasPermission("gg.command." + c))
                    list.add(c);
            }
        } else if (args.length == 2) {
            if (sender.hasPermission("gg.command." + args[0])) {
                args[1] = args[1].toLowerCase();
                if (args[0].equals("remove") || args[0].equals("info") || args[0].equals("removespawn") || (sender instanceof Player && args[0].equals("setspawn"))) {
                    for (String g : games.keySet()) {
                        if (g.startsWith(args[1]))
                            list.add(g);
                    }
                }
            }
        }
        return new ArrayList<>(list);
    }
}
 */
