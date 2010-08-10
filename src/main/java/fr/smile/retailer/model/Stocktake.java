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
 * Persistence class that reflect the process of verification the stock/warehouse state
 * <br/>
 * <br/>
 * Used to show real quantities of products in stock at current time
 * 
 * @author ansam
 *
 */
@PersistenceCapable
public class Stocktake implements KeyEnabled {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Date date;

	@Persistent
	private Key storeKey;
	
	@Persistent
	private Key zreportKey;
	
	@Persistent(mappedBy = "stocktake", defaultFetchGroup = "true")
	@Element(dependent = "true")
	private List<StocktakeItem> items;
	
	@Persistent
	private Blob xls;
	
	@NotPersistent
	private Store store;

	@NotPersistent
	private ZReport zreport;
	
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

	public void setItems(List<StocktakeItem> items) {
		this.items = items;
	}

	public List<StocktakeItem> getItems() {
		return items;
	}

	@Override
	public Key getKey() {
		return key;
	}

	public void setZreportKey(Key zreportKey) {
		this.zreportKey = zreportKey;
	}

	public Key getZreportKey() {
		return zreportKey;
	}

	public void setZreport(ZReport zreport) {
		if (zreport != null) {
			this.zreportKey = zreport.getKey();
		}
		this.zreport = zreport;
	}

	public ZReport getZreport() {
		return zreport;
	}

	public void setXLSBlob(Blob xls) {
		this.xls = xls;
	}

	public Blob getXLSBlob() {
		return xls;
	}
	
}
