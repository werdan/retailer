package fr.smile.retailer.services.interfaces;

import java.util.List;

import fr.smile.retailer.dao.XLSLineModelFactory;
import fr.smile.retailer.model.ZReportItem;

public interface IZReportService extends XLSLineModelFactory {

	public ZReportItem createItem(List<String> values);

}