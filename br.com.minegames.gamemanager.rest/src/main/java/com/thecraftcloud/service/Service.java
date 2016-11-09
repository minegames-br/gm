package com.thecraftcloud.service;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.thecraftcloud.core.domain.TransferObject;

public class Service {
	protected EntityManagerFactory emf; 
	protected EntityManager em;
	protected boolean slave = false; 
	
	public Service() {
		
	}
	
	public Service(EntityManager em) {
		this.em = em;
		this.slave = true;
	}
	
	protected void startTransaction() {
		if(!this.slave) {
			this.emf = Persistence.createEntityManagerFactory("game-manager");
			this.em = emf.createEntityManager();
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
	
	public TransferObject findByUUID(Class _class, UUID uuid) {
		startTransaction();
		TransferObject domain = em.find(_class, uuid);
		commitTransaction();
		return domain;
	}
	
	public void commitTransaction() {
		if(!this.slave) {
			try{
				em.getTransaction().commit();
			}finally{
				em.close();
				emf.close();
			}
		}
	}
	

}
