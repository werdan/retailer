package fr.smile.retailer.web.controller.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Jama.Matrix;

import com.google.appengine.api.datastore.Key;

import fr.smile.retailer.dao.Filter;
import fr.smile.retailer.dao.StoreDAO;
import fr.smile.retailer.dao.interfaces.IDeliveryDAO;
import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.dao.interfaces.ISupplierDAO;
import fr.smile.retailer.model.Delivery;
import fr.smile.retailer.model.DeliveryItem;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.Supplier;
import fr.smile.retailer.web.propertyeditors.StoreEditor;
import fr.smile.retailer.web.view.Cell;
import fr.smile.retailer.web.view.Row;
import fr.smile.retailer.web.view.Table;

@Controller
public class SupplierCostsReportController {

	public final static String REPORTS_PREFIX = "reports/";
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ISupplierDAO supplierDao;

	@Autowired
	private IDeliveryDAO deliveryDao;

	@Autowired
	private IStoreDAO storeDao;
	
	@Autowired
	private IProductDAO productDao;
	
	@Autowired
	private StoreEditor storePropEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Store.class, storePropEditor);
		dataBinder.setIgnoreInvalidFields(true);
		dataBinder.setIgnoreUnknownFields(false);
	}

	@RequestMapping(value = "/reports/suppliercosts/input", method = RequestMethod.GET)
	public ModelAndView getSupplierCostsReportInput() {
		ModelAndView mav = new ModelAndView(REPORTS_PREFIX + "suppliercosts_input");
		mav.addObject("stores", storeDao.findAll());
		return mav;
	}
	
	@RequestMapping(value = "/reports/suppliercosts", method = RequestMethod.GET)
	public ModelAndView getSupplierCostsReport(@RequestParam("store") Store store) {
		ModelAndView mav = new ModelAndView(REPORTS_PREFIX + "suppliercosts");
		mav.addObject("store", store);
		Filter filter = new Filter("storeKey == targetStoreKey", "com.google.appengine.api.datastore.Key targetStoreKey", store.getKey());
		List<Delivery> deliveries = deliveryDao.findFiltered(filter);
		List<Key> supplierKeys = new ArrayList<Key>();
		List<Key> products = new ArrayList<Key>();
		Map<Key,List<DeliveryVectorContainer>> suppliersVectors = new HashMap<Key,List<DeliveryVectorContainer>>();

		logger.debug("Define list of products");
		for (Delivery delivery: deliveries) {
			for (DeliveryItem item: delivery.getItems()) {
				if (!item.isTrashed() && !products.contains(item.getProductKey())) {
					logger.debug("Added product with key = " + item.getProductKey());
					products.add(item.getProductKey());
				}
			}
		}
		Collections.sort(products);
		logger.debug("Final table will include " + products.size() + " product lines");
		
		logger.debug("Definition of vectors for each delivery in supplier");
		for (Delivery delivery: deliveries) {
			List<DeliveryVectorContainer> deliveryVClist;
			if (!supplierKeys.contains(delivery.getSupplierKey())) {
				supplierKeys.add(delivery.getSupplierKey());
				deliveryVClist = new ArrayList<DeliveryVectorContainer>();
			} else {
				deliveryVClist = suppliersVectors.get(delivery.getSupplierKey());
			}
			DeliveryVectorContainer deliveryVC = createDeliveryVectorContainer(products, delivery);
			deliveryVClist.add(deliveryVC);
			suppliersVectors.put(delivery.getSupplierKey(), deliveryVClist);
		}
		
		Map<Supplier,SupplierVectorContainer> suppliersCalculatedVectors = new HashMap<Supplier,SupplierVectorContainer>();

		for (Key supplierKey : supplierKeys) {
			SupplierVectorContainer svc = calculateAvgPricesAndCosts(suppliersVectors.get(supplierKey));		
			suppliersCalculatedVectors.put(supplierDao.getEntityByKey(supplierKey), svc);
		}
		
		mav.addObject("productsVersusSuppliers",createTableFromSupplierVectors(suppliersCalculatedVectors, products));
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	private Table createTableFromSupplierVectors(Map<Supplier,SupplierVectorContainer> suppliersCalculatedVectors, List<Key> products) {
		Table table = new Table();
    	
		logger.debug("Creating first header row");
		Row firstRow = new Row();
		firstRow.addCell(new Cell(""));
    	Iterator it = suppliersCalculatedVectors.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry<Supplier,SupplierVectorContainer> entry = (Map.Entry<Supplier,SupplierVectorContainer>) it.next();
    		String cellValue = ((Supplier)entry.getKey()).getName() + " prices";
    		logger.debug("Adding cell with value " + cellValue);
    		firstRow.addCell(new Cell(cellValue));
    		cellValue = ((Supplier)entry.getKey()).getName() + " costs";
    		logger.debug("Adding cell with value " + cellValue);
    		firstRow.addCell(new Cell(cellValue));
    	}
    	table.addRow(firstRow);
    	
    	logger.debug("Adding a row per product");
		int i = 0;
    	for (Key productKey: products) {
	    	Row row = new Row();
	    	Product product = productDao.getEntityByKey(productKey);
	    	logger.debug("Adding product name " + product.getName());
	    	row.addCell(new Cell(product.getName()));
	    	it = suppliersCalculatedVectors.entrySet().iterator();
	    	while (it.hasNext()) {
	    		Map.Entry<Supplier,SupplierVectorContainer> entry = (Map.Entry<Supplier,SupplierVectorContainer>) it.next();
	    		SupplierVectorContainer svc = entry.getValue();
	    		try {
	    			BigDecimal price = new BigDecimal(svc.avgPrices.get(0, i));
	    			logger.debug("Adding price " + price.setScale(3, BigDecimal.ROUND_HALF_EVEN).toPlainString());
	    			row.addCell(new Cell(price.setScale(3, BigDecimal.ROUND_HALF_EVEN).toPlainString()));
	    		} catch (NumberFormatException e) {
	    			logger.debug("Adding empty price cell");
	    			row.addCell(new Cell(""));
	    		}
	    		try {
	    			BigDecimal cost = new BigDecimal(svc.avgCosts.get(0, i));
			    	logger.debug("Adding cost " + cost.setScale(3, BigDecimal.ROUND_HALF_EVEN).toPlainString());
	    			row.addCell(new Cell(cost.setScale(3, BigDecimal.ROUND_HALF_EVEN).toPlainString()));
	    		} catch (NumberFormatException e) {
			    	logger.debug("Adding empty cost cell");
	    			row.addCell(new Cell(""));
	    		}
	    	}
	    	i++;
	    	logger.debug("Adding product row");
	    	table.addRow(row);
	    }
		return table;
	}

	private SupplierVectorContainer calculateAvgPricesAndCosts(
			List<DeliveryVectorContainer> dvcList) {
		Matrix pricesProductSum = new Matrix(1, dvcList.get(0).costs.getArray()[0].length);
		Matrix costsProductSum = new Matrix(1, dvcList.get(0).costs.getArray()[0].length);
		Matrix quantitiesSum= new Matrix(1, dvcList.get(0).costs.getArray()[0].length);

		for (DeliveryVectorContainer dvc : dvcList) {
			pricesProductSum = pricesProductSum.plus(dvc.prices.arrayTimes(dvc.quantities));
			costsProductSum = costsProductSum.plus(dvc.costs.arrayTimes(dvc.quantities));
			quantitiesSum = quantitiesSum.plus(dvc.quantities);
		}
		return new SupplierVectorContainer(pricesProductSum.arrayRightDivide(quantitiesSum), costsProductSum.arrayRightDivide(quantitiesSum));
	}

	private DeliveryVectorContainer createDeliveryVectorContainer(
			List<Key> products, Delivery delivery) {

		logger.debug("Creating vectors for Delivery " + delivery.getDate() + " of supplier " + delivery.getSupplierKey());
		
		DeliveryVectorContainer dvc = new DeliveryVectorContainer();
		logger.debug("Creating matrices 1x" + products.size());
		dvc.prices = new Matrix(1, products.size());
		dvc.costs = new Matrix(1, products.size());
		dvc.quantities = new Matrix(1, products.size());

		logger.debug("Filling matrices with 0");
		int i=0;
		for (Key productKey: products) {
			dvc.prices.set(0, i, 0);
			dvc.costs.set(0, i, 0);
			dvc.quantities.set(0, i, 0);
			i++;
		}
		for (DeliveryItem item : delivery.getItems()) {
			if (products.indexOf(item.getProductKey()) >= 0 ) {
				int productPosition = products.indexOf(item.getProductKey());
				logger.debug("Set price matrix element 0," + productPosition +  " with value " + item.getPrice().doubleValue());
				dvc.prices.set(0, productPosition, item.getPrice().doubleValue());
	
				logger.debug("Set costs matrix element 0," + productPosition + " with value " + item.getCost().doubleValue());
				dvc.costs.set(0, productPosition, item.getCost().doubleValue());
				
				logger.debug("Set quantities matrix element 0," + productPosition + " with value " + item.getQuantity().doubleValue());
				dvc.quantities.set(0, productPosition, item.getQuantity().doubleValue());
				
			} else {
				logger.debug("Skipping product from delivery as it is not in the list");
			}
		}
		return dvc;
	}

	private class DeliveryVectorContainer {
		public Matrix prices;
		public Matrix costs;
		public Matrix quantities;
	}
	
	private class SupplierVectorContainer {
		public Matrix avgPrices;
		public Matrix avgCosts;
		
		public SupplierVectorContainer(Matrix prices, Matrix costs) {
			this.avgCosts = costs;
			this.avgPrices = prices;
		}
	
	}
	
	
}
