package fr.smile.retailer.model;

import java.math.BigDecimal;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Linking class between Stockmove and Product. Literally provides information about the line in Stockmove file
 * @author ansam
 *
 */
@PersistenceCapable
public class StockmoveItem implements XLSLineModel {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Stockmove stockmove;
		
	@Persistent
	private Key oldProductKey;

	@NotPersistent
	private Product oldProduct;

	@Persistent
	private Key newProductKey;

	@NotPersistent
	private Product newProduct;

	/**
	 * Quantity of products moved
	 */
	@Persistent
	private BigDecimal quantity;

	public void setOldProduct(Product product) {
		this.oldProduct = product;
		if (product != null) {
			this.setOldProductKey(product.getKey());
		}
	}

	public Product getOldProduct() {
		return oldProduct;
	}

	public void setNewProduct(Product product) {
		this.newProduct = product;
		if (product != null) {
			this.setNewProductKey(product.getKey());
		}
	}

	public Product getBewProduct() {
		return newProduct;
	}

	
	public void setStockmove(Stockmove stockmove) {
		this.stockmove = stockmove;
	}

	public Stockmove getStockmove() {
		return stockmove;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setOldProductKey(Key productKey) {
		this.oldProductKey = productKey;
	}

	public Key getOldProductKey() {
		return oldProductKey;
	}

	public void setNewProductKey(Key productKey) {
		this.newProductKey = productKey;
	}

	public Key getNewProductKey() {
		return newProductKey;
	}


}
