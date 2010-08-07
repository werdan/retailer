package fr.smile.retailer.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class DeliveryServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
    private IDeliveryService deliveryService;

	@Autowired
	private IProductDAO productDao;
    
	private LocalServiceTestHelper helper = null;

    @AfterMethod
    public void tearDown() {
        helper.tearDown();
    }
	
	@BeforeMethod
	public void setUp() {
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setUp();
	}

    
    @Test
    public void testCreateItemAndCalculateCosts() {
    	Product pr1 = new Product();
    	pr1.setName("test1");
    	pr1.setCode("1");
    	productDao.save(pr1);
    	
    	Product pr2 = new Product();
    	pr2.setName("test2");
    	pr2.setCode("2");
    	productDao.save(pr2);
    	
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
    	values.add("нет");
    	DeliveryItem item = (DeliveryItem) deliveryService.createItem(values);
    	
    	
    	Assert.assertTrue(item.getProduct().getName().equals("test1"));
    	Assert.assertTrue(item.getQuantity().compareTo(BigDecimal.valueOf(24.25d)) == 0);
    	Assert.assertTrue(item.getPrice().compareTo(BigDecimal.valueOf(33.123d)) == 0);
    	
    	values = new ArrayList<String>();
    	values.add("2");
    	//Quantity
    	values.add(BigDecimal.valueOf(12.25d).toString());
    	//Price
    	values.add(BigDecimal.valueOf(33.123d).toString());
    	//Trashed
    	values.add("нет");
    	DeliveryItem item2 = (DeliveryItem) deliveryService.createItem(values);

    	values = new ArrayList<String>();
    	values.add("3");
    	//Quantity
    	values.add(BigDecimal.valueOf(2.3d).toString());
    	//Price
    	values.add(BigDecimal.valueOf(0).toString());
    	//Trashed
    	values.add("да");
    	DeliveryItem item3 = (DeliveryItem) deliveryService.createItem(values);
    	
    	Delivery delivery = new Delivery();
    	List<DeliveryItem> deliveryItems = new ArrayList<DeliveryItem>();
    	deliveryItems.add(item);
    	deliveryItems.add(item2);
    	deliveryItems.add(item3);
    	delivery.setItems(deliveryItems);

    	for (DeliveryItem itemToCheck: delivery.getItems()) {
    		Assert.assertNull(itemToCheck.getCost());
    	}
    	
    	deliveryService.calculateCosts(delivery);
    	
    	for (DeliveryItem itemToCheck: delivery.getItems()) {
    		Assert.assertNotNull(itemToCheck.getCost());
    		BigDecimal targetCost = itemToCheck.getPrice().multiply(BigDecimal.valueOf(1.063013699d));
    		targetCost = targetCost.setScale(3, BigDecimal.ROUND_HALF_EVEN);
    		Assert.assertTrue(itemToCheck.getCost().compareTo(targetCost) == 0);
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
