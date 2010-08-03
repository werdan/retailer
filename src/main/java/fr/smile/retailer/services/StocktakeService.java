package fr.smile.retailer.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.StocktakeItem;
import fr.smile.retailer.services.interfaces.IStocktakeService;

public class StocktakeService implements IStocktakeService {

	@Autowired
	private IProductDAO productDao;
	
	@Override
	public StocktakeItem createItem(List<String> values) {
		StocktakeItem stocktakeItem = new StocktakeItem();
		
		String code = values.get(0);

		BigDecimal quantity = NumberUtils.createBigDecimal(values.get(2));
		quantity = quantity.setScale(3, BigDecimal.ROUND_HALF_EVEN);

		Product product = productDao.getByCode(new Integer(new Double(code).intValue()).toString());
		stocktakeItem.setProduct(product);
		stocktakeItem.setQuantity(quantity);
		return stocktakeItem;
	}

	@Override
	public List<String> getListHeaderLineCells() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("productCode");
		columnNames.add("productName");
		columnNames.add("quantity");
		return columnNames;
	}

	@Override
	public void calculateCosts(Stocktake take) {
		
	}
}
