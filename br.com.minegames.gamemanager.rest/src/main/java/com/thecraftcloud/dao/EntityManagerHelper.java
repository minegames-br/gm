package com.thecraftcloud.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {

	private static EntityManagerHelper me;
	protected EntityManagerFactory emf;

	
	private EntityManagerHelper() {
		this.emf = Persistence.createEntityManagerFactory("game-manager");
	}
	
	public static EntityManagerHelper getInstance() {
		if(me == null) {
			me = new EntityManagerHelper();
		}
		return me;
	}
	
	public EntityManager createEntityManager() {
		return emf.createEntityManager();
	}

	
	public void commitTransaction(EntityManager em) {
		try{
			em.getTransaction().commit();
		}finally{
			em.close();
		}
	}

}
