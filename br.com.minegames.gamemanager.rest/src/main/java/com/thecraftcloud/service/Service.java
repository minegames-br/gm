package com.thecraftcloud.service;

import java.util.UUID;

import javax.persistence.EntityManager;

import com.thecraftcloud.core.domain.TransferObject;
import com.thecraftcloud.dao.EntityManagerHelper;

public class Service {
	protected EntityManager em;
	protected boolean slave = false; 
	
	public Service() {
		this.em = EntityManagerHelper.getInstance().createEntityManager();
	}
	
	public Service(EntityManager em) {
		this.em = em;
		this.slave = true;
	}
	
	protected void startTransaction() {
		if(!this.slave) {
			this.em = EntityManagerHelper.getInstance().createEntityManager();
			em.getTransaction().begin();
		}
	}
	
	public TransferObject merge(TransferObject domain) {
		startTransaction();
		TransferObject to = em.merge(domain);
		commitTransaction();
		return to;
	}
	
	public void create(TransferObject domain) {
		startTransaction();
		em.persist(domain);
		commitTransaction();
	}
	
	public void delete(TransferObject domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
	}
	
	public TransferObject findByUUID(Class _class, UUID uuid) {
		startTransaction();
		TransferObject domain = em.find(_class, uuid);
		commitTransaction();
		return domain;
	}
	
	public void commitTransaction() {
		if(!this.slave) {
			EntityManagerHelper.getInstance().commitTransaction(this.em);
		}
	}
	
	

}
