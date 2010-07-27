package fr.smile.retailer.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Linking class between Delivery and Product. Literally provides information about the line in Delivery file
 * @author ansam
 *
 */
@PersistenceCapable
public class DeliveryItem {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Delivery delivery;
		
	@Persistent
	private Product product;
	

	public void setProduct(Product product) {
		this.product = product;
		if (product != null) {
			this.key = product.getKey();
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Delivery getDelivery() {
		return delivery;
	}

}
