package fr.smile.retailer.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.model.Delivery;
import fr.smile.retailer.model.DeliveryItem;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.XLSLineModel;
import fr.smile.retailer.services.interfaces.IDeliveryService;
import fr.smile.retailer.utils.CustomNumberUtils;

public class DeliveryService implements IDeliveryService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private IProductDAO productDao;
	
	@Override
	public void calculateCosts(Delivery delivery) {
		BigDecimal koefLeakage = calculateLeakageKoef(delivery);
		List<DeliveryItem> deliveryItems = new ArrayList<DeliveryItem>();
		for (DeliveryItem deliveryItem : delivery.getItems()) {
			deliveryItem.setCost(deliveryItem.getPrice().multiply(koefLeakage).setScale(3, BigDecimal.ROUND_HALF_EVEN));
			deliveryItems.add(deliveryItem);
		}
		delivery.setItems(deliveryItems);
	}

	/**
	 * Calculate ratio of all delivered products quantity to products put to stock 
	 * (i.e. that part of products are thrown just after delivery)
	 * @param delivery
	 * @return
	 */
	private BigDecimal calculateLeakageKoef(Delivery delivery) {
		
		BigDecimal sumAll = new BigDecimal(0);
		BigDecimal sumNotTrashed = new BigDecimal(0);
		for (DeliveryItem deliveryItem : delivery.getItems()) {
			sumAll = sumAll.add(deliveryItem.getQuantity());
			if (!deliveryItem.isTrashed()) {
				sumNotTrashed = sumNotTrashed.add(deliveryItem.getQuantity());
			}
		}
		return sumAll.divide(sumNotTrashed,3, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	public XLSLineModel createItem(List<String> values) throws IllegalArgumentException {
		DeliveryItem deliveryItem = new DeliveryItem();
		
		String code = values.get(0);

		BigDecimal quantity = CustomNumberUtils.createBigDecimal(values.get(1));
		quantity = quantity.setScale(3, BigDecimal.ROUND_HALF_EVEN);

		Product product = productDao.getByCode(CustomNumberUtils.createIntegerViaDouble(code).toString());
		if (product == null) {
			throw new IllegalArgumentException("Product with code = " + CustomNumberUtils.createIntegerViaDouble(code).toString() + " not found");
		}
		deliveryItem.setProduct(product);
		deliveryItem.setQuantity(quantity);
		
		deliveryItem.setPrice(CustomNumberUtils.createBigDecimal(values.get(2)));
		deliveryItem.setTrashed(StringUtils.contains(values.get(3), "да"));
		return deliveryItem;
	}

	@Override
	public List<String> getListHeaderLineCells() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("Код товара");
		columnNames.add("Приход, кг");
		columnNames.add("Входная цена, грн");
		columnNames.add("Выбрасываем");
		return columnNames;
	}
}
