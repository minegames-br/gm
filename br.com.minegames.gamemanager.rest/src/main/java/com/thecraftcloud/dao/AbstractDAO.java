package com.thecraftcloud.dao;

import javax.persistence.EntityManager;

public abstract class AbstractDAO {

	protected EntityManager em;
	
	public AbstractDAO(EntityManager em) {
		this.em = em;
	}
	
}
