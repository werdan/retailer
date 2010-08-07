package fr.smile.retailer.dao;

import java.util.List;

import fr.smile.retailer.model.XLSLineModel;

/**
 * DAO that can create internal items from List<String>
 * @author ansam
 *
 */
public interface XLSLineModelFactory {
	XLSLineModel createItem(List<String> values) throws IllegalArgumentException;
	List<String> getListHeaderLineCells();
}
