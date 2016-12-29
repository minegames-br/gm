package com.thecraftcloud.minigame.domain;

public class MyCloudCraftGame {
	protected GameState state;
	protected long gameStartTime;
	protected Level level;

	public MyCloudCraftGame() {

		// mudar o state do jogo para esperar jogadores entrarem
		this.state = GameState.WAITING;
		this.level = new Level();

	}

	public void shutDown() {
		state = GameState.SHUTDOWN;
	}

	public void start() {
		state = GameState.RUNNING;
		this.gameStartTime = System.currentTimeMillis();
	}

	public boolean isShuttingDown() {
		return this.state.equals(GameState.SHUTDOWN);
	}

	public void startCountDown() {
		this.state = GameState.STARTING;
	}

	public boolean isStarting() {
		return this.state.equals(GameState.STARTING);
	}

	public boolean isStarted() {
		return this.state.equals(GameState.RUNNING);
	}

	public GameState getState() {
		return this.state;
	}

	public boolean isWaitingPlayers() {
		return this.state.equals(GameState.WAITING) || this.state.equals(GameState.STARTING);
	}

	public void endGame() {
		this.state = GameState.GAMEOVER;
	}

	public boolean isOver() {
		return this.state.equals(GameState.GAMEOVER);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void levelUp() {
		this.level = new Level(this.level);
		this.state = GameState.RUNNING;
	}

	public void setGameState(GameState state) {
		this.state = state;
	}

	public boolean hasLevels() {
		return false;
	}

	public long getGameStartTime() {
		return gameStartTime;
	}

	public void setGameStartTime(long gameStartTime) {
		this.gameStartTime = gameStartTime;
	}

	public void setState(GameState state) {
		this.state = state;
	}

}
