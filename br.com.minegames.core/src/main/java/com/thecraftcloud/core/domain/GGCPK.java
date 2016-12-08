package com.thecraftcloud.core.domain;

import java.io.Serializable;

public class GGCPK implements Serializable {

	private GameConfig gameConfig;
	private Game game;
	
	public GGCPK() {
		
	}
	
	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof GGCPK ) {
			GGCPK pk = (GGCPK) obj;
			
			if(! pk.getGame().equals(this.getGame()) ) {
				return false;
			}
			
			if( ! pk.getGameConfig().equals(this.getGameConfig())) {
				return false;
			}
			return true;
		}
		return false;
	}

    @Override
    public int hashCode() {
        return this.getGame().hashCode() + this.getGameConfig().hashCode();
    }

	public GameConfig getGameConfig() {
		return gameConfig;
	}

	public void setGameConfig(GameConfig gameConfig) {
		this.gameConfig = gameConfig;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
    

	
	
	
}
