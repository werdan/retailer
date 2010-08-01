package fr.smile.retailer.model;

import java.math.BigDecimal;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Linking class between ZReport and Product. Literally provides information about the line in ZReport file
 * @author ansam
 *
 */
@PersistenceCapable
public class ZReportItem implements KeyEnabled, XLSLineModel {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private ZReport zreport;
		
	@Persistent
	private BigDecimal quantity;
	
	@Persistent
	private Key productKey;
	
	@NotPersistent
	private Product product;
	
	public void setZReport(ZReport zreport) {
		this.zreport = zreport;
	}

	public ZReport getZReport() {
		return zreport;
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

}
