package com.thecraftcloud.gungame;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;

import com.thecraftcloud.core.domain.Local;
import com.thecraftcloud.core.logging.MGLogger;
import com.thecraftcloud.core.util.Utils;
import com.thecraftcloud.domain.GamePlayer;
import com.thecraftcloud.gungame.domain.GunGame;
import com.thecraftcloud.gungame.domain.GunGamePlayer;
import com.thecraftcloud.gungame.listener.PlayerDeath;
import com.thecraftcloud.gungame.service.GunGameConfigService;
import com.thecraftcloud.gungame.service.GunGamePlayerService;
import com.thecraftcloud.gungame.task.SpawnBonusItemTask;
import com.thecraftcloud.plugin.TheCraftCloudConfig;
import com.thecraftcloud.plugin.TheCraftCloudMiniGameAbstract;

/**
 * Created by joaoemilio@gmail.com on Nov 26, 2016
 */
public class GameController extends TheCraftCloudMiniGameAbstract {
	private Integer gameDuration;
	private Integer spawnBonusItemThreadID;
	private SpawnBonusItemTask spawnBonusItemTask;
	private GunGamePlayerService gunGamePlayerService = new GunGamePlayerService(this);

    @Override
    public void onEnable() {

		super.onEnable();
		
    	//ao criar, o jogo fica imediatamente esperando jogadores
		this.myCloudCraftGame = new GunGame();
		spawnBonusItemTask = new SpawnBonusItemTask(this);
		
		//Carregar configuracoes especificas do Gun Game
		GunGameConfigService.getInstance().loadConfig();
    }

	@Override
	public void startGameEngine() {
		super.startGameEngine();

		gunGamePlayerService.setupPlayersToStartGame();
		
		// Enviar jogadores para a Arena
		gunGamePlayerService.teleportPlayersToArena();
		
		// Iniciar threads do jogo
		BukkitScheduler scheduler = getServer().getScheduler();
		this.spawnBonusItemThreadID = scheduler.scheduleSyncRepeatingTask(this, this.spawnBonusItemTask, 200L, 200L);
	}
	
	@Override
	public void init(World _world, Local _lobby) {
		super.init(_world, _lobby);
	}
	
	@Override
	public boolean shouldEndGame() {
    	//Terminar o jogo caso não tenha mais jogadores
    	if( this.getLivePlayers().size() == 0  && this.myCloudCraftGame.isStarted()) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6EndGameTask - No more players"));
            return true;
    	}
    	
    	//Terminar o jogo caso tenha alcançado o limite de tempo
    	long currentTime = System.currentTimeMillis();
    	long duration = ( currentTime - this.getMyCloudCraftGame().getGameStartTime() ) / 1000;
		this.gameDuration = (Integer)this.configService.getGameConfigInstance(TheCraftCloudConfig.GAME_DURATION_IN_SECONDS);

    	if( duration >= this.gameDuration  ) {
            Bukkit.getConsoleSender().sendMessage(Utils.color("&6EndGameTask - TimeOver: " + duration + " > " + this.gameDuration ));
    		return true;
    	}
    	return false;
	}
	
	/**
	 * Quando esse método executar, o jogo terá terminado com um vencedor e/ou o
	 * tempo terá acabado.
	 */
	@Override
	public void endGame() {
		super.endGame();
		if (this.myCloudCraftGame.isStarted()) {
			this.myCloudCraftGame.endGame();
		}
		MGLogger.info("Game.endGame");

		// Terminar threads do jogo
		Bukkit.getScheduler().cancelTask(this.spawnBonusItemThreadID);

		for (GamePlayer gp : livePlayers) {
			Player player = gp.getPlayer();
			player.getInventory().clear();
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			player.sendMessage("Você fez " + gp.getPoint() + " pontos.");
		}

	}

	@Override
	public boolean isLastLevel() {
		return false;
	}

	@Override
	public void levelUp() {
		return;
	}

	@Override
	public GamePlayer createGamePlayer() {
		return new GunGamePlayer();
	}
	
	@Override
	protected void registerListeners() {
		super.registerListeners();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerDeath(this), this);
	}


}
