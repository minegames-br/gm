package com.thecraftcloud.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.thecraftcloud.core.admin.domain.ActionDTO;
import com.thecraftcloud.core.domain.AdminQueue;
import com.thecraftcloud.core.domain.MineCraftPlayer;
import com.thecraftcloud.core.domain.PlayerStatus;
import com.thecraftcloud.core.domain.RequestStatus;
import com.thecraftcloud.core.domain.ServerInstance;
import com.thecraftcloud.core.json.JSONParser;

public class AdminService extends Service {

	public AdminService() {
		
	}
	
	public AdminService(EntityManager em) {
		super(em);
	}
	
	public void teleportPlayerToLobby(String name) {
		startTransaction();
		MineCraftPlayerService pService = new MineCraftPlayerService(this.em);
		MineCraftPlayer mcp = pService.findByName(name);
		
		mcp.setStatus(PlayerStatus.LOBBY);
		this.em.merge(mcp);
		
		AdminQueue aq = new AdminQueue();
		
		ActionDTO action = new ActionDTO();
		action.setName(ActionDTO.TELEPORT_PLAYER);
		action.setPlayer(mcp);
		
		ServerService sService = new ServerService(this.em);
		ServerInstance lobbyServer = sService.findByName("mglobby");
		log("server: " + lobbyServer );
		action.setServer(lobbyServer);

		//The action will be sent to the DB so that TCC Manager can process it
		String json = JSONParser.getInstance().toJSONString(action);
		aq.setAction(json);
		aq.setRequestTime(Calendar.getInstance());
		aq.setStatus(RequestStatus.OPEN);
		
		this.em.persist(aq);
		
		commitTransaction();
	}

	public Collection<AdminQueue> findAllRequestsOpened() {
		return findAllRequestsByStatus(RequestStatus.OPEN);
	}
	
	public Collection<AdminQueue> findAllRequestsByStatus(RequestStatus status) {
		startTransaction();
		Query query = em.createQuery("SELECT aq FROM AdminQueue aq where aq.status = :status");
		query.setParameter("status", status);
		Collection<AdminQueue> list = (Collection<AdminQueue>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void markRequestComplete(String uuid) {
		startTransaction();
		AdminService aService = new AdminService(this.em);
		AdminQueue qa = (AdminQueue)aService.findByUUID(AdminQueue.class, UUID.fromString(uuid) );
		qa.setResponseTime(Calendar.getInstance());
		qa.setStatus(RequestStatus.CLOSED);
		this.em.merge(qa);
		commitTransaction();
	}

	
}
