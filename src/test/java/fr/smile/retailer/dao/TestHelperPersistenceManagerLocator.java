package fr.smile.retailer.dao;

import javax.jdo.PersistenceManager;

public class TestHelperPersistenceManagerLocator implements PersistenceManagerLocator {

	private PersistenceManager pm;
	
	@Override
	public PersistenceManager getPersistenceManager() {
		return this.pm;
	}
	
}
