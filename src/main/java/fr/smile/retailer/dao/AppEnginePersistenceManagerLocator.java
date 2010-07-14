package fr.smile.retailer.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import fr.smile.retailer.dao.interfaces.PersistenceManagerLocator;

public class AppEnginePersistenceManagerLocator implements PersistenceManagerLocator {

	private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public PersistenceManager getPersistenceManager() {
		return pmfInstance.getPersistenceManager();
	}

}
