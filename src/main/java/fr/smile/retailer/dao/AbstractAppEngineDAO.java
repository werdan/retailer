package fr.smile.retailer.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Key;

import fr.smile.retailer.dao.interfaces.GenericDAO;
import fr.smile.retailer.dao.interfaces.PersistenceManagerLocator;
import fr.smile.retailer.model.KeyEnabled;


public abstract class AbstractAppEngineDAO<T extends KeyEnabled> implements GenericDAO<T> {

	protected Class<KeyEnabled> modelClass;

    @SuppressWarnings("unchecked")
	protected AbstractAppEngineDAO() {
        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            this.modelClass = (Class<KeyEnabled>) parameterizedType.getActualTypeArguments()[0];
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	public List<T> findFiltered(Filter filter) {
		return findFilteredExt(filter, null, null);
	}
	
	/**
	 * 
	 * Find instances according to filter provided and optional sort order.<br/>
     * <br/>
	 * Example of two-parameters filter:<br/>
	 * <br/>
	 * <code>
	 * Filter filter = new Filter("date < nowDate && storeKey == targetStoreKey", 
	 *			"java.util.Date nowDate, com.google.appengine.api.datastore.Key targetStoreKey", cal.getTime());
	 *  		filter.setParamValue2(take.getStoreKey());
     * </code>
	 * </p>
	 */
	@Override
	public List<T> findFiltered(Filter filter, String orderBy) {
		return findFilteredExt(filter, orderBy, null);
	}
		
	@SuppressWarnings("unchecked")
	List<T> findFilteredExt(Filter filter, String orderBy, String range) {
	    PersistenceManager pm = getPersistenceManagerLocator().getPersistenceManager();
		Query query = pm.newQuery("SELECT FROM " + modelClass.getName());
		query.setFilter(filter.getFilterExpession());
		query.declareParameters(filter.getParamExpression());
		if (!StringUtils.isBlank(range)) {
			query.setRange(range);
			//FIXME This is only for range="0,1" => Calculate unique from range expression
			query.setUnique(true);
		}
		if (orderBy != null) {
			query.setOrdering(orderBy);
		}
		try {
			Object result = null;
			switch (filter.getParamNumber()) {
				case 1: { 
					result = query.execute(filter.getParamValue());
					break;
				}
				case 2: { 
					result = query.execute(filter.getParamValue(),filter.getParamValue2());
					break;
				}
				case 3: { 
					result = query.execute(filter.getParamValue(),filter.getParamValue2(), filter.getParamValue3());
					break;
				}
				default: 
					throw new IllegalArgumentException("Filter accepts only 1 up to 3 parameters, currently got " + filter.getParamNumber());
			}
			List<T> list = new ArrayList<T>();
			if (!StringUtils.isBlank(range) && result instanceof KeyEnabled) {
				list.add((T) result);
			} else if (result instanceof List){
				list = (List<T>) result;
				/*
				 * Querying list size to lazy load entities
				 * http://code.google.com/p/datanucleus-appengine/issues/detail?id=46
				 */
				list.size();
			} else {
				throw new IllegalArgumentException("Unexpected result of Query, accepting only classes that implement KeyEnabled or List interfaces");
			}
			return list;
        } finally {
        	query.closeAll();
        	pm.close();
        }
	}

	@Override
	public List<T> findFilteredIsNull(String property, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findUniqueFiltered(Filter filter) {
		return findUniqueFiltered(filter, null);
	}

	@Override
	public T findUniqueFiltered(Filter filter, String orderBy) {
			List<T> list = findFilteredExt(filter, orderBy, "0,1");
			if (list != null && list.get(0) != null) {
				return initUnownedRelations(list.get(0));
			} else {
				return null;
			}
	}

	@Override
	public void refresh(Object entity) {
		// TODO Auto-generated method stub
		
	}
	
}
