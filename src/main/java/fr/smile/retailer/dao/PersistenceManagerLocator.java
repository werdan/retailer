package fr.smile.retailer.dao;

import javax.jdo.PersistenceManager;

public interface PersistenceManagerLocator {
	
	public PersistenceManager getPersistenceManager();
}
