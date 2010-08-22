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
 * Persistence class that reflect the process of moving product from code to code in stock
 */

@PersistenceCapable
public class Stockmove implements KeyEnabled {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Date date;

	@Persistent
	private Key storeKey;
	
	@Persistent(mappedBy = "stockmove", defaultFetchGroup = "true")
	@Element(dependent = "true")
	private List<StockmoveItem> items;
	
	@Persistent
	private Blob xls;
	
	@NotPersistent
	private Store store;

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

	public void setItems(List<StockmoveItem> items) {
		this.items = items;
	}

	public List<StockmoveItem> getItems() {
		return items;
	}

	@Override
	public Key getKey() {
		return key;
	}
	
	public void setXLSBlob(Blob xls) {
		this.xls = xls;
	}

	public Blob getXLSBlob() {
		return xls;
	}

	
}
