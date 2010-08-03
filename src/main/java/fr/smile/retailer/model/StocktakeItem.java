package fr.smile.retailer.model;

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Linking class between Stocktake and Product. Literally provides information about the line in Stocktake file
 * @author ansam
 *
 */
@PersistenceCapable
public class StocktakeItem implements KeyEnabled, XLSLineModel {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Stocktake stocktake;
		
	@Persistent
	private BigDecimal quantity;
	
	@Persistent
	private Key productKey;
	
	@Persistent
	private BigDecimal cost;
	
	@NotPersistent
	private Product product;
	
	public void setStocktake(Stocktake stocktake) {
		this.stocktake = stocktake;
	}

	public Stocktake getStocktake() {
		return stocktake;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product != null) {
			this.productKey = product.getKey();
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setProductKey(Key productKey) {
		this.productKey = productKey;
	}

	public Key getProductKey() {
		return productKey;
	}

	@Override
	public Key getKey() {
		return key;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getCost() {
		return cost;
	}

}
