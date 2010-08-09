package fr.smile.retailer.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Product implements KeyEnabled{

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;

	@NotPersistent
	private int cachedHashCode;

	@Persistent
	private String code;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public boolean equals(Object o) {
		if (o instanceof Product) {
			Product os = (Product) o;
			if (getName() != null && os.getName() != null) {
				return getName().equals(os.getName());
			} else {
				return getName() == os.getName();
			}
		}
		return false;
	}
	
	public int hashCode() {
		if (cachedHashCode == 0 ) {
			cachedHashCode = (getName() != null ? getName().hashCode() : 0);
		}
		return cachedHashCode;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	
	@Override
	public Key getKey() {
		return key;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
