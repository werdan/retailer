package fr.smile.retailer.dao;

/**
 * JavaBean class for keeping reference on wheather we are in production environment or not
 * @author ansam
 *
 */
public class ProductionEnv {
	private boolean productionEnv = false;

	public void setProductionEnv(boolean prod) {
		this.productionEnv = prod;
	}
	
	public boolean isProductionEnv() {
		return this.productionEnv;
	}	
}
