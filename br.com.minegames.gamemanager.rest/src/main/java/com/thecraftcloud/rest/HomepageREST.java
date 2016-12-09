package com.thecraftcloud.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/homepage")
public class HomepageREST extends REST {
	
	@GET
	@Path("/jogos/content")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		log("uuid recebido: ");
		String json = "[{\"image_url\":\"http://www.minegames.com.br/images/jogo_buildbattle.png\", \"text\":\"A MineGames est� muito contente em poder oferecer mais essa modalidade de Jogo. N�o deixe de conferir em nossa rede a mais nova vers�o do jogo GunGame. Est� muito divertido!! Um jogo de PVP para at� 20 jogadores..\"},{\"image_url\":\"http://www.minegames.com.br/images/jogo_skywars.png\", \"text\":\"Skywars � um dos jogos mais populares em servidores MineCraft. N�s n�o poder�amos ficar de fora, n�o � verdade? A diferen�a, � que nossos modos de jogo s�o mais justos e divertidos para VIPs e jogadores eventuais. Experimente e nos diga o que voc� achou. Beleza?\"}]";
	    return Response.ok( json , MediaType.APPLICATION_JSON).build();
	}	
	
}
