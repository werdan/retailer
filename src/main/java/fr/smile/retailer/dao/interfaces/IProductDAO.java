package fr.smile.retailer.dao.interfaces;

import fr.smile.retailer.model.Product;

public interface IProductDAO extends GenericDAO<Product>{

	public Product getByCode(String code);

}
