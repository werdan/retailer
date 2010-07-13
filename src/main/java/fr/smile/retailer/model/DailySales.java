package fr.smile.retailer.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DailySales {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	

	@Persistent
	private BigDecimal sum;
	
	@Persistent
	private Date date;

	@NotPersistent
	private int cachedHashCode;	
	
	
	/**
	 * Sets Time part of Date to 0;
	 * @param date
	 * @return
	 */
	public final static Date resetHoursMinutesSeconds(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
	
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
		this.date = resetHoursMinutesSeconds(date);
	}

	//TODO: correct equals implementation
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
			String hcString = this.sum.toString() + new Long(this.date.getTime()).toString();
			cachedHashCode = hcString.hashCode();
		}
		return cachedHashCode;
	}
}
