package fr.smile.retailer.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.model.Delivery;
import fr.smile.retailer.model.DeliveryItem;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.services.interfaces.IDeliveryService;
import fr.smile.retailer.utils.SimpleXLSParser;
import fr.smile.retailer.utils.XLSParser;
import fr.smile.retailer.web.controller.AbstractControllerTest;

public class DeliveryServiceTest extends AbstractControllerTest {

	@Autowired
    private IDeliveryService deliveryService;
 
       
    @SuppressWarnings("unchecked")
	@Test
    public void testCreateItemAndCalculateCosts2() throws IOException {
    	createProducts(new String[] {"1","2","3","4","5","6","7"});
    	Resource deliveryFile = loader.getResource("classpath:/testfiles/Delivery3.xls");
    	XLSParser parser = new SimpleXLSParser();
		List<DeliveryItem> deliveryItems= (List<DeliveryItem>) parser.parse(deliveryFile.getInputStream(), deliveryService);
		Delivery delivery = new Delivery();
    	delivery.setItems(deliveryItems);

    	for (DeliveryItem itemToCheck: delivery.getItems()) {
    		Assert.assertNull(itemToCheck.getCost());
    	}
    	
    	deliveryService.calculateCosts(delivery);
    	
    	Assert.assertTrue(delivery.getItems().size() == 3);
    	
    	for (DeliveryItem itemToCheck: delivery.getItems()) {
    		Assert.assertNotNull(itemToCheck.getCost());
    		if (itemToCheck.getProduct().getCode().equals("1")) {
    			Assert.assertTrue(itemToCheck.getCost().compareTo(BigDecimal.valueOf(38.238d)) == 0);
    		} else if (itemToCheck.getProduct().getCode().equals("2")) {
    			Assert.assertTrue(itemToCheck.getCost().compareTo(BigDecimal.valueOf(38.238d)) == 0);
    		} else if (itemToCheck.getProduct().getCode().equals("5")) {
    			Assert.assertTrue(itemToCheck.getCost().compareTo(BigDecimal.valueOf(0d)) == 0);
    		}
    	}
    }
    
    
    @Test
    public void createItemTrashedFalse() {
    	Product pr3 = new Product();
    	pr3.setName("test3");
    	pr3.setCode("3");
    	productDao.save(pr3);
    	
    	List<String> values = new ArrayList<String>();
    	values.add("3");
    	//Quantity
    	values.add(BigDecimal.valueOf(24.25d).toString());
    	//Price
    	values.add(BigDecimal.valueOf(33.123d).toString());
    	//Trashed
    	values.add("не знаю");
    	DeliveryItem item = (DeliveryItem) deliveryService.createItem(values);
    	
    	Assert.assertFalse(item.isTrashed());
    }

    @Test
    public void createItemTrashedTrue() {
    	Product pr3 = new Product();
    	pr3.setName("test3");
    	pr3.setCode("3");
    	productDao.save(pr3);
    	
    	List<String> values = new ArrayList<String>();
    	values.add("3");
    	//Quantity
    	values.add(BigDecimal.valueOf(24.25d).toString());
    	//Price
    	values.add(BigDecimal.valueOf(33.123d).toString());
    	//Trashed
    	values.add("да");
    	DeliveryItem item = (DeliveryItem) deliveryService.createItem(values);
    	
    	Assert.assertTrue(item.isTrashed());
    }
    
    @Test
    public void createItemNoSuchProduct() {
    	Product pr3 = new Product();
    	pr3.setName("test3");
    	pr3.setCode("3");
    	productDao.save(pr3);
    	
    	List<String> values = new ArrayList<String>();
    	values.add("1");
    	//Quantity
    	values.add(BigDecimal.valueOf(24.25d).toString());
    	//Price
    	values.add(BigDecimal.valueOf(33.123d).toString());
    	//Trashed
    	values.add("да");
    	try {
    		DeliveryItem item = (DeliveryItem) deliveryService.createItem(values);
    	} catch (IllegalArgumentException e) {
    		//it is ok to have this expection
    	}

    }

}
