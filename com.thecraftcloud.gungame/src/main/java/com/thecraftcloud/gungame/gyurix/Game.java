package com.thecraftcloud.gungame.gyurix;

import gyurix.configfile.PostLoadable;

/**
 * Created by GyuriX on 2016. 09. 22..
 */
/*	
public class Game implements Runnable, PostLoadable {

    public String name, winner = "N/A";
    public HashSet<LocationData> signs = new HashSet<>();
    public ArrayList<LocationData> spawns = new ArrayList<LocationData>();
    @ConfigOptions(serialize = false)
    public HashMap<String, LocationData> playerSpawns = new HashMap<>();
    @ConfigOptions(serialize = false)
    public int count, rid;
    @ConfigOptions(serialize = false)
    public GameState state = GameState.Waiting;
    @ConfigOptions(serialize = false)
    public HashMap<Player, PlayerData> datas = new HashMap<>();
    @ConfigOptions(serialize = false)
    public HashMap<String, Integer> levels = new HashMap<>();

    public Game(String name) {
        this.name = name;
        this.state = GameState.Waiting;
        postLoad();
    }

    public void join(Player plr) {
        if (state == GameState.Ingame) {
            lang.msg(plr, "join.ingame", "game", name);
            return;
        }
        if (spawns.size() == datas.size()) {
            lang.msg(plr, "join.full", "game", name);
            return;
        }
        if (playerGames.containsKey(plr.getName())) {
            lang.msg(plr, "join.already");
            return;
        }
        lang.msg(plr, "join", "game", name);
        LocationData ld = spawns.get(levels.size());
        datas.put(plr, new PlayerData(plr, GameMode.ADVENTURE, ld.getLocation()));
        plr.addPotionEffect(new PotionEffect(SLOW, 10000000, 1000, false, false));
        levels.put(plr.getName(), 1);
        playerGames.put(plr.getName(), this);
        playerSpawns.put(plr.getName(), ld);
        lastLoc.put(plr.getName(), plr.getLocation());
        for (Command c : GunGameConfig.join)
            c.execute(plr);
        if (state == GameState.Waiting && minPlayer <= datas.size()) {
            state = GameState.Starting;
            count = GunGameConfig.startTime;
        }
    }

    public void kill() {
        state = GameState.Waiting;
        count = 0;
        levels.clear();
        playerSpawns.clear();
        for (PlayerData pd : datas.values()) {
            pd.restore();
            playerGames.remove(pd.plr.getName());
        }
        datas.clear();
    }

    @Override
    public void postLoad() {
        rid = SU.sch.scheduleSyncRepeatingTask(pl, this, 20, 20);
    }

    public void quit(Player plr) {
        levels.remove(plr.getName());
        playerSpawns.remove(plr.getName());
        PlayerData pd = datas.remove(plr);
        pd.restore();
    }

    public void updateSigns() {
    	if( GunGameConfig.signs == null || GunGameConfig.signs.size() == 0) {
    		return;
    	}
    	
    	if(state == null) {
    		return;
    	}
    	
        SignConfig sc = GunGameConfig.signs.get(state.name());
        boolean change = false;
        Iterator<LocationData> it = signs.iterator();
        while (it.hasNext()) {
            LocationData ld = it.next();
            try {
                sc.set((Sign) ld.getBlock().getState(), "arena", name, "count", count,
                        "need", String.valueOf(max(0, minPlayer - levels.size())),
                        "winner", winner);
            } catch (Throwable e) {
                SU.log(pl, "§cDetected sign remove at §e" + ld);
                it.remove();
                GunGameConfig.joinSigns.remove(ld);
                change = true;
            }
        }
        if (change)
            kf.save();
    }

    public ArrayList<Entry<String, Integer>> getTopList() {
        TreeSet<Entry<String, Integer>> topset = new TreeSet<>(new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                int c = o2.getValue().compareTo(o1.getValue());
                return c == 0 ? o1.getKey().compareTo(o2.getKey()) : c;
            }
        });
        topset.addAll(levels.entrySet());
        return new ArrayList<>(topset);
    }

    @Override
    public void run() {
    	if(state == null ) {
    		return;
    	}
        updateSigns();
        switch (state) {
            case Waiting:
                executeAll(datas.keySet(), null, GunGameConfig.waiting);
                return;
            case Starting:
                if (--count == 0) {
                    for (Player p : datas.keySet())
                        p.removePotionEffect(SLOW);
                    count = GunGameConfig.gameTime;
                    executeAll(datas.keySet(), null, GunGameConfig.ingame);
                    state = GameState.Ingame;
                    return;
                }
                executeAll(datas.keySet(), null, GunGameConfig.starting);
                return;
            case Ingame:
                if (--count == 0) {
                    count = GunGameConfig.endTime;
                    executeAll(datas.keySet(), null, GunGameConfig.finish);
                    ArrayList<Entry<String, Integer>> topList = getTopList();
                    int id = 1;
                    for (Entry<String, Integer> e : topList) {
                        ArrayList<Command> cmds = GunGameConfig.rewards.get(id++);
                        if (cmds == null)
                            cmds = GunGameConfig.rewards.get(0);
                        executeAll(Bukkit.getPlayer(e.getKey()), cmds);
                    }
                    state = GameState.Finish;
                    return;
                }
                executeAll(datas.keySet(), null, GunGameConfig.ingame);
                return;
            case Finish:
                if (--count == 0) {
                    kill();
                    return;
                }
                for (LocationData ld : spawns) {
                    World w = ld.getWorld();
                    Firework fw = (Firework) w.spawnEntity(ld.getLocation(), EntityType.FIREWORK);
                    FireworkMeta fm = fw.getFireworkMeta();
                    fm.setPower(SU.rand.nextInt(2));
                    int max = SU.rand.nextInt(3) + 1;
                    for (int i = 0; i < max; i++)
                        fm.addEffect(FireworkEffect.builder()
                                .flicker(SU.rand.nextBoolean())
                                .trail(SU.rand.nextBoolean())
                                .withColor(SU.randColor(0.3, 1, 0.5, 0.9))
                                .withColor(SU.randColor(0.3, 1, 0.5, 0.9))
                                .withFade(SU.randColor(0.3, 1, 0.7, 0.9))
                                .withFade(SU.randColor(0.3, 1, 0.7, 0.9)).build());
                    fw.setFireworkMeta(fm);
                }
                return;
        }
    }
    
}
 */
