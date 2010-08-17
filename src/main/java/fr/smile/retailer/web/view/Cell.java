package fr.smile.retailer.web.view;

public class Cell {

	private String value;
	private int cachedHashCode;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Cell(String value) {
		this.value = value;
	}

	public Cell() {}
	
	public boolean equals(Object o) {
		if (o instanceof Cell) {
			Cell ds = (Cell) o;
			if (this.getValue().equals(ds.getValue())) {
				return true;
			}
		} 
		return false;
	}
	
	public int hashCode() {
		if (cachedHashCode == 0 ) {
			cachedHashCode = this.getValue().hashCode();
		}
		return cachedHashCode;
	}
	
	public String toString() {
		return this.value;
	}

}
