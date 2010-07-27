package fr.smile.retailer.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.model.Product;

public class ProductDAO extends AbstractAppEngineDAO<Product> implements IProductDAO{

	@Override
	@SuppressWarnings("unchecked")  
	public Product getByCode(String code) {
	    PersistenceManager pm = getPersistenceManagerLocator().getPersistenceManager();
	    Query query = pm.newQuery(modelClass);
	    query.setFilter("code == productCode");
	    query.declareParameters("String productCode");
		try {
			List<Product> list = (List<Product>) query.execute(code);
			if (list.size() > 1) {
				throw new IllegalStateException("There should be only one instance, got : " + list.size());
			} else if (list.size() == 0) {
				return null;
			}
			return list.get(0);
        } finally {
            query.closeAll();
        }

	}


}
