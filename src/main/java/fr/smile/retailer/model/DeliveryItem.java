package fr.smile.retailer.model;

import java.math.BigDecimal;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
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
public class DeliveryItem implements XLSLineModel {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Delivery delivery;
		
	@Persistent
	private Key productKey;

	@NotPersistent
	private Product product;

	/**
	 * Net price after excluding of products that can be used (trashed == true)
	 */
	@Persistent
	private BigDecimal cost;

	/**
	 * Quantity of products delivered
	 */
	@Persistent
	private BigDecimal quantity;

	/**
	 * Price as stated by supplier
	 */
	@Persistent
	private BigDecimal price;
	
	/**
	 * If product can not be selled, it is trashed (e.g., bones)
	 */
	@Persistent
	private boolean trashed;

	public void setProduct(Product product) {
		this.product = product;
		if (product != null) {
			this.productKey = product.getKey();
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

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}


}
