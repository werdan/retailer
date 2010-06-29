package fr.smile.retailer.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.beans.factory.annotation.Autowired;


@SuppressWarnings("rawtypes")
public abstract class AbstractAppEngineDAO<T> implements GenericDAO<T> {

	private Class modelClass;

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
	    Query query = pm.newQuery(modelClass);
		try {
            return (List<T>) query.execute();            
        } finally {
            query.closeAll();
        }
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
