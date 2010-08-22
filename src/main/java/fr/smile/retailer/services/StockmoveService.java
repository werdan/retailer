package fr.smile.retailer.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.StockmoveItem;
import fr.smile.retailer.model.XLSLineModel;
import fr.smile.retailer.services.interfaces.IStockmoveService;
import fr.smile.retailer.utils.CustomNumberUtils;

public class StockmoveService implements IStockmoveService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private IProductDAO productDao;
	
	@Override
	public XLSLineModel createItem(List<String> values) throws IllegalArgumentException {
		StockmoveItem stockmoveItem = new StockmoveItem();
		
		String oldCode = values.get(0);
		String newCode = values.get(1);

		BigDecimal quantity = CustomNumberUtils.createBigDecimal(values.get(2));
		quantity = quantity.setScale(3, BigDecimal.ROUND_HALF_EVEN);
		if (quantity.compareTo(BigDecimal.valueOf(0)) == 0) {
			throw new IllegalArgumentException("Product with code = " + CustomNumberUtils.createIntegerViaDouble(oldCode).toString() + " is not imported as quantity = 0");
		}
		
		Product oldProduct = productDao.getByCode(CustomNumberUtils.createIntegerViaDouble(oldCode).toString());
		if (oldProduct == null) {
			throw new IllegalArgumentException("Product with code = " + CustomNumberUtils.createIntegerViaDouble(oldCode).toString() + " not found");
		}

		Product newProduct = productDao.getByCode(CustomNumberUtils.createIntegerViaDouble(newCode).toString());
		if (newProduct == null) {
			throw new IllegalArgumentException("Product with code = " + CustomNumberUtils.createIntegerViaDouble(oldCode).toString() + " not found");
		}
		
		stockmoveItem.setOldProduct(oldProduct);
		stockmoveItem.setNewProduct(newProduct);
		stockmoveItem.setQuantity(quantity);
		return stockmoveItem;
	}

	@Override
	public List<String> getListHeaderLineCells() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("productCode");
		columnNames.add("NewProductCode");
		columnNames.add("quantity");
		return columnNames;
	}
}
