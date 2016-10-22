package br.com.minegames.gamemanager.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Service {
	protected EntityManagerFactory emf = Persistence.createEntityManagerFactory("game-manager"); 
	protected EntityManager em;
		
	protected void startTransaction() {
		this.em = emf.createEntityManager();
		em.getTransaction().begin();
	}
	
	public void commitTransaction() {
		em.getTransaction().commit();
		em.close();
	}
	

}
