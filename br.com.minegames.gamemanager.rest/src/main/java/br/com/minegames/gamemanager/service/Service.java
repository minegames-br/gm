package br.com.minegames.gamemanager.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Service {
	protected EntityManagerFactory emf = Persistence.createEntityManagerFactory("game-manager"); 
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
			this.em = emf.createEntityManager();
			em.getTransaction().begin();
		}
	}
	
	public void commitTransaction() {
		if(!this.slave) {
			try{
				em.getTransaction().commit();
			}finally{
				em.close();
			}
		}
	}
	

}
