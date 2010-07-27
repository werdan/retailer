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
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.StocktakeItem;
import fr.smile.retailer.services.interfaces.IStocktakeService;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class StocktakeServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
    private IStocktakeService stocktakeService;

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
    public void testCreateItem() {
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
    	values.add("2");
    	values.add("Ошеек свиной");
    	values.add(BigDecimal.valueOf(24.25d).toString());

    	StocktakeItem item = (StocktakeItem) stocktakeService.createItem(values);
    	Assert.assertTrue(item.getProduct().getName().equals("test2"));
    	Assert.assertTrue(item.getQuantity().compareTo(BigDecimal.valueOf(24.25d)) == 0);
    	
    }
}
