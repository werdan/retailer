package fr.smile.retailer.dao.interfaces;

import java.util.List;

import com.google.appengine.api.datastore.Key;


/**
 * Interface for AbstractDAO to be implemented by every DAO
 * either by inheritance or overriding
 *
 * @author ansam
 */
public interface GenericDAO<T> {

	T initUnownedRelations(T entity);
	
    T getEntityByKey(Key key);

    void save(Object entity);

    void saveOrUpdate(Object entity);

    void update(Object entity);

    void delete(Object entity);

//    void deleteById(ID id);

    List<T> findAll();

    List<T> findAll(String orderBy);

    List<T> findFiltered(String property,
                         Object filter);

    List<T> findFiltered(String property,
                         Object filter, String orderBy);

    /**
     * Returns list of items that have property set to NULL
     *
     * @param property domain class proeprty
     * @param orderBy  order by clause
     * @return filtered list
     */
    List<T> findFilteredIsNull(String property, String orderBy);

    Object findUniqueFiltered(String property,
                              Object filter);

    Object findUniqueFiltered(String property,
                              Object filter, String orderBy);

    void refresh(Object entity);
    
}