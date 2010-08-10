package fr.smile.retailer.model;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

/**
 * Persistence class that reflect the process of putting new product to stock
 */

@PersistenceCapable
public class Delivery implements KeyEnabled {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Date date;

	@Persistent
	private Key storeKey;
	
	@Persistent
	private Key supplierKey;
	
	@Persistent(mappedBy = "delivery", defaultFetchGroup = "true")
	@Element(dependent = "true")
	private List<DeliveryItem> items;
	
	@Persistent
	private Blob xls;
	
	@NotPersistent
	private Store store;

	@NotPersistent
	private Supplier supplier;

	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public void setStoreKey(Key storeKey) {
		this.storeKey = storeKey;
	}

	public Key getStoreKey() {
		return storeKey;
	}

	public void setStore(Store store) {
		this.store = store;
		if (store != null) {
			this.storeKey = store.getKey();
		}
	}

	public Store getStore() {
		return store;
	}

	public void setItems(List<DeliveryItem> items) {
		this.items = items;
	}

	public List<DeliveryItem> getItems() {
		return items;
	}

	@Override
	public Key getKey() {
		return key;
	}
	
	public void setSupplierKey(Key supplierKey) {
		this.supplierKey = supplierKey;
	}

	public Key getSupplierKey() {
		return supplierKey;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
		if (supplier != null) {
			this.supplierKey = supplier.getKey();
		}
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setXLSBlob(Blob xls) {
		this.xls = xls;
	}

	public Blob getXLSBlob() {
		return xls;
	}

	
}
