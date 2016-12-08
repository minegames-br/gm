package com.thecraftcloud.service;

public class ConfigService extends Service {
/*
	public UUID create(Config domain) {
		startTransaction();
		ConfigDAO dao = new ConfigDAO(em);
		dao.save(domain);
		commitTransaction();
		Logger.getLogger(ConfigService.class).info("uuid: " + domain.getConfig_uuid());
		return domain.getConfig_uuid();
	}
	
	public Config find(UUID uuid) {
		startTransaction();
		ConfigDAO dao = new ConfigDAO(em);
		Config domain = dao.find(uuid);
		commitTransaction();
		return domain;
	}
	
	public Collection<Config> findAll() {
		startTransaction();
		Query query = em.createQuery("SELECT c FROM Config c");
		Collection<Config> list = (Collection<Config>) query.getResultList();
		commitTransaction();
		return list;
	}

	public void delete(Config domain) {
		startTransaction();
		em.remove(domain);
		commitTransaction();
		Logger.getLogger(ConfigService.class).info("uuid: " + domain.getConfig_uuid() + " deletado");
	}
*/	
}
