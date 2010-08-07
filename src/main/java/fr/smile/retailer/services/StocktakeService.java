package fr.smile.retailer.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.retailer.dao.Filter;
import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.dao.interfaces.IStocktakeDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.StocktakeItem;
import fr.smile.retailer.services.interfaces.IStocktakeService;
import fr.smile.retailer.utils.CustomNumberUtils;

public class StocktakeService implements IStocktakeService {

	@Autowired
	private IProductDAO productDao;
	
	@Autowired
	private IStocktakeDAO stocktakeDao;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public StocktakeItem createItem(List<String> values) throws IllegalArgumentException{
		StocktakeItem stocktakeItem = new StocktakeItem();
		
		String code = values.get(0);

		BigDecimal quantity = CustomNumberUtils.createBigDecimal(values.get(2));
		quantity = quantity.setScale(3, BigDecimal.ROUND_HALF_EVEN);

		Product product = productDao.getByCode(CustomNumberUtils.createIntegerViaDouble(code).toString());
		if (product == null) {
			throw new IllegalArgumentException("Product with code = " + CustomNumberUtils.createIntegerViaDouble(code).toString() + " not found");
		}
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
		logger.debug("Fetching the last Stocktake for period definition");
		Calendar cal = new GregorianCalendar();
		Filter filter = new Filter("date < nowDate && storeKey == targetStoreKey", 
				"java.util.Date nowDate, com.google.appengine.api.datastore.Key targetStoreKey", cal.getTime());
		filter.setParamValue2(take.getStoreKey());
		Stocktake lastStocktake = stocktakeDao.findUniqueFiltered(filter, "date desc");
		
	}
}
