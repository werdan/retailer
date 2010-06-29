package fr.smile.retailer.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public abstract class AbstractTestHelperAppEngineDAO<T> implements GenericDAO<T> {
	private final static DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	
	
	public void setPersistenceManagerLocator(PersistenceManagerLocator pml) {
	}
	
	@Override
	public void save(Object entity) {
		ds.put(new Entity("dailySales"));
	}

	@Override
	public void saveOrUpdate(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> findAll() {
		Iterable<T> it = (Iterable<T>) ds.prepare(new Query("dailySales")).asIterable();
		it.iterator();
		List<T> list = new ArrayList<T>();
		while(it.iterator().hasNext()) {
			list.add(it.iterator().next());
		}
		return list;
	}

	@Override
	public List<T> findAll(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findFiltered(String property, Object filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findFiltered(String property, Object filter, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findFilteredIsNull(String property, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findUniqueFiltered(String property, Object filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findUniqueFiltered(String property, Object filter, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh(Object entity) {
		// TODO Auto-generated method stub
		
	}
	
}
