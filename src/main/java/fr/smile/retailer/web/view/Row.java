package fr.smile.retailer.web.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representation of a table row
 * @author ansam
 *
 */
public class Row {
	
	private List<Cell> cells = new ArrayList<Cell>();
	private int cachedHashCode;

	public void setCells(Cell[] cells) {
		this.cells.addAll( Arrays.asList(cells));
	}

	public Cell[] getCellsAsArray() {
		return (Cell[]) cells.toArray();
	}

	public List<Cell> getCells() {
		return cells;
	}

	public Row(String[] cellValues) {
		for (String cellValue: cellValues) {
			cells.add(new Cell(cellValue));
		}
	}
	
	public void addCell(Cell cell) {
		this.cells.add(cell);
	}

	public Row() {}
	
	public boolean equals(Object o) {
		if (o instanceof Row) {
			Row ds = (Row) o;
			if (this.getCells().equals(ds.getCells())) {
				return true;
			}
		} 
		return false;
	}
	
	public int hashCode() {
		if (cachedHashCode == 0 ) {
			cachedHashCode = this.getCells().hashCode();
		}
		return cachedHashCode;
	}

}
