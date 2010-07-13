package fr.smile.retailer.dao;

import java.util.Date;

import fr.smile.retailer.model.DailySales;

public interface IDailySalesDAO extends GenericDAO<DailySales>{

	DailySales getByDate(Date date);	
	public void setPersistenceManagerLocator(PersistenceManagerLocator pml);
	public PersistenceManagerLocator getPersistenceManagerLocator();

}
