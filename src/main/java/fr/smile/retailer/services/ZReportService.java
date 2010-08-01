package fr.smile.retailer.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.ZReportItem;
import fr.smile.retailer.services.interfaces.IZReportService;

public class ZReportService implements IZReportService {

	@Autowired
	private IProductDAO productDao;
	
	@Override
	public ZReportItem createItem(List<String> values) {
		ZReportItem zreportItem = new ZReportItem();
		
		
		String code = values.get(0);
		
		String quantityToParse = values.get(2).isEmpty() ? "0" : values.get(2);
		BigDecimal quantity = new BigDecimal(quantityToParse);
		quantity = quantity.setScale(3, BigDecimal.ROUND_HALF_EVEN);
		
		Product product = productDao.getByCode(new Integer(new Double(code).intValue()).toString());
		zreportItem.setProduct(product);
		zreportItem.setQuantity(quantity);
		return zreportItem;
	}

	@Override
	public List<String> getListHeaderLineCells() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("КОД (ШТРИХ-КОД):");
		columnNames.add("НАЙМЕНУВАННЯ:");
		columnNames.add("ЗАЛИШОК:");
		return columnNames;
	}
}
