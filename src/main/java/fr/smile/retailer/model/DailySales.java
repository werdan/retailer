package fr.smile.retailer.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.time.DateUtils;

import com.google.appengine.api.datastore.Key;

import fr.smile.retailer.web.propertyeditors.DateEditor;

@PersistenceCapable
public class DailySales implements KeyEnabled {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private BigDecimal sum;
	
	@Persistent
	private Date date;

	@Persistent
	private Key storeKey;
	
	@NotPersistent
	private int cachedHashCode;

	@NotPersistent
	private Store store;
	
	public Key getKey() {
		return key;
	}
	
	public BigDecimal getSum() {
		return sum;
	}
	
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = DateUtils.round(date, Calendar.DAY_OF_MONTH);
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

	/** 
	 * Used in JSP to represent date of DailySales as bean property
	 * @return
	 */
	public String getShortDate() {
		DateEditor de = new DateEditor();
		de.setValue(this.date);
		return de.getAsText();
	}
	
	public boolean equals(Object o) {
		if (o instanceof DailySales) {
			DailySales ds = (DailySales) o;
			if ((this.getSum() !=null && ds.getSum() != null && this.getSum().equals(ds.getSum()) ||
					this.getSum() == ds.getSum()) &&
					(this.getDate() != null && ds.getDate() != null && this.getDate().equals(ds.getDate())) || 
					this.getDate() == ds.getDate()) {
				return true;
			}
		} 
		return false;
	}
	
	public int hashCode() {
		if (cachedHashCode == 0 ) {
			String hcString = (this.sum != null ? this.sum.toString() : "") + (this.date != null ? new Long(this.date.getTime()).toString() : "");
			cachedHashCode = hcString.hashCode();
		}
		return cachedHashCode;
	}
}
