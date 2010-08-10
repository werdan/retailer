package fr.smile.retailer.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Persistence class that describes Supplier
 */

@PersistenceCapable
public class Supplier implements KeyEnabled {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;

	@NotPersistent
	private int cachedHashCode;

	public Supplier(String name) {
		this.name= name;
	}

	public Supplier() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Key getKey() {
		return key;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Supplier) {
			Supplier ds = (Supplier) o;
			if (this.getName().equals(ds.getName())){
				return true;
			}
		} 
		return false;
	}
	
	public int hashCode() {
		if (cachedHashCode == 0 ) {
			cachedHashCode = name.hashCode();
		}
		return cachedHashCode;
	}

}
