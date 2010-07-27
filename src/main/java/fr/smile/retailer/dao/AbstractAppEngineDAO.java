package fr.smile.retailer.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Key;

import fr.smile.retailer.dao.interfaces.GenericDAO;
import fr.smile.retailer.dao.interfaces.PersistenceManagerLocator;
import fr.smile.retailer.model.KeyEnabled;


@SuppressWarnings("rawtypes")
public abstract class AbstractAppEngineDAO<T extends KeyEnabled> implements GenericDAO<T> {

	protected Class modelClass;

    protected AbstractAppEngineDAO() {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            this.modelClass = (Class) parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new RuntimeException(this.getClass().getName() + " are not parametrized by generic type.");
        }
    }

	@Autowired
	private PersistenceManagerLocator pmlocator;
	
	public void setPersistenceManagerLocator(PersistenceManagerLocator pml) {
		this.pmlocator = pml;
	}
	
	public PersistenceManagerLocator getPersistenceManagerLocator() {
		return this.pmlocator;
	}
	
	@Override
	public T initUnownedRelations(T entity) {
		return entity;
	}
	
    @SuppressWarnings("unchecked")
	public T getEntityByKey(Key key) { 

	    PersistenceManager pm = pmlocator.getPersistenceManager();
		Query query = pm.newQuery("SELECT FROM " + modelClass.getName());
		query.setFilter("key == entityKey");
		query.declareParameters("com.google.appengine.api.datastore.Key entityKey");
	    query.setUnique(true);
		try {
	    	T result = (T) query.execute(key);
	    	return initUnownedRelations(result);
        } finally {
        	query.closeAll();
        	pm.close();
        }
    }

	
	@Override
	public void save(Object entity) {
	    PersistenceManager pm = pmlocator.getPersistenceManager();
		try {
            pm.makePersistent(entity);
        } finally {
            pm.close();
        }
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

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
	    PersistenceManager pm = pmlocator.getPersistenceManager();
	    Extent extent = pm.getExtent(modelClass, false);
		Iterator it = extent.iterator();
		List<T> result = new ArrayList<T>();
		while (it.hasNext()) {
			T resultItem = (T) it.next();
			result.add(initUnownedRelations(resultItem));
		}
		extent.closeAll();
		pm.close();
		return result;
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
