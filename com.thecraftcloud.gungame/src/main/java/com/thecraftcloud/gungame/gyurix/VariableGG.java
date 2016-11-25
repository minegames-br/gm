package com.thecraftcloud.gungame.gyurix;
/**

import gyurix.api.VariableAPI.VariableHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map.Entry;

import static org.apache.commons.lang.StringUtils.leftPad;

/**
 * Created by GyuriX on 2016. 09. 22..
public class VariableGG implements VariableHandler {
    public Object getValue(Player plr, ArrayList<Object> args, Object[] eArgs) {
        String sub = StringUtils.join(args, "");
        Game g = GameController.playerGames.get(plr.getName());
        if (g == null)
            return "NOT IN GAME";
        switch (sub) {
            case "count":
                return g.count / 60 + ":" + leftPad(String.valueOf(g.count % 60), 2, '0');
            case "ingame":
                return g.levels.size();
            case "level":
                return g.levels.get(plr.getName());
            case "need":
                return Math.max(0, GunGameConfig.minPlayer - g.levels.size());
            case "state":
                return GameController.lang.get(plr, "state." + g.state);
        }
        if (sub.startsWith("top")) {
            ArrayList<Entry<String, Integer>> topList = g.getTopList();
            if (sub.startsWith("topp")) {
                int id = Integer.valueOf(sub.substring(4)) - 1;
                if (topList.size() <= id)
                    return 0;
                return topList.get(id).getValue();
            }
            int id = Integer.valueOf(sub.substring(3)) - 1;
            if (topList.size() <= id)
                return "N/A";
            return topList.get(id).getKey();
        }
        return "WRONG SUB: " + sub;
    }
}
 */
