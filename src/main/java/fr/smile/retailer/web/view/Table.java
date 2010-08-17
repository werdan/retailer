package fr.smile.retailer.web.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
	private List<Row> rows = new ArrayList<Row>();
	private int cachedHashCode;

	public void setRows(Row[] rows) {
		this.rows.addAll(Arrays.asList(rows));
	}

	public Row[] getRowsAsArray() {
		return (Row[]) rows.toArray();
	}
	
	public List<Row> getRows() {
		return rows;
	}
	
	public void addRow(Row row) {
		this.rows.add(row);
	}
	
	public boolean equals(Object o) {
		if (o instanceof Table) {
			Table ds = (Table) o;
			if (this.getRows().equals(ds.getRows())) {
				return true;
			}
		} 
		return false;
	}
	
	public int hashCode() {
		if (cachedHashCode == 0 ) {
			cachedHashCode = this.getRows().hashCode();
		}
		return cachedHashCode;
	}
}
