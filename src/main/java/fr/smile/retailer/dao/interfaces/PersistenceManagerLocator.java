package fr.smile.retailer.dao.interfaces;

import javax.jdo.PersistenceManager;

public interface PersistenceManagerLocator {
	
	public PersistenceManager getPersistenceManager();
}
