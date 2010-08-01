package fr.smile.retailer.model;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Persistence class that reflect ZReport of EKKA
 * <br/>
 * <br/>
 * Used to show quantities of products in stock at current time according to data in EKKA
 * 
 * @author ansam
 *
 */
@PersistenceCapable
public class ZReport implements KeyEnabled {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Date date;

	@Persistent
	private Key storeKey;
	
	@Persistent(mappedBy = "zreport", defaultFetchGroup = "true")
	@Element(dependent = "true")
	private List<ZReportItem> items;
	
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

	public void setItems(List<ZReportItem> items) {
		this.items = items;
	}

	public List<ZReportItem> getItems() {
		return items;
	}

	@Override
	public Key getKey() {
		return key;
	}
	
}
