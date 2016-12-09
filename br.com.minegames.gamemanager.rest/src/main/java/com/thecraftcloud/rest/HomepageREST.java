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
		String json = "[{\"image_url\":\"http://www.minegames.com.br/images/jogo_buildbattle.png\", \"text\":\"A MineGames está muito contente em poder oferecer mais essa modalidade de Jogo. Não deixe de conferir em nossa rede a mais nova versão do jogo GunGame. Está muito divertido!! Um jogo de PVP para até 20 jogadores..\"},{\"image_url\":\"http://www.minegames.com.br/images/jogo_skywars.png\", \"text\":\"Skywars é um dos jogos mais populares em servidores MineCraft. Nós não poderíamos ficar de fora, não é verdade? A diferença, é que nossos modos de jogo são mais justos e divertidos para VIPs e jogadores eventuais. Experimente e nos diga o que você achou. Beleza?\"}]";
	    return Response.ok( json , MediaType.APPLICATION_JSON).build();
	}	
	
}
