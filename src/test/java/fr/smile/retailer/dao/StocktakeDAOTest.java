package fr.smile.retailer.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import fr.smile.retailer.dao.interfaces.IStocktakeDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.StocktakeItem;
import fr.smile.retailer.model.Store;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class StocktakeDAOTest extends AbstractTestNGSpringContextTests {

	private LocalServiceTestHelper helper = null;
	
	@Autowired
	private IStoreDAO storeDao;

	@Autowired
	private IStocktakeDAO stocktakeDao;

	@Autowired
	private IProductDAO productDao;
	
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
	public void testGetStocktakeItems() {
		Stocktake stocktake = new Stocktake();
		Calendar cal = new GregorianCalendar();
		stocktake.setDate(cal.getTime());
		
		Store store = new Store("test");
		storeDao.save(store);
		
		stocktake.setStore(store);
		stocktake.setStoreKey(store.getKey());

		List<Product> productsInDBlist = productDao.findAll();
		Assert.assertTrue(productsInDBlist.size() == 0);
		
		Product product1 = new Product();
		product1.setName("testProduct1");
		productDao.save(product1);
		Product productTest = productDao.getEntityByKey(product1.getKey());
		Assert.assertNotNull(productTest);
		
		
		Product product2 = new Product();
		product2.setName("testProduct2");
		productDao.save(product2);
		
		Assert.assertNotSame(product1.getKey(), product2.getKey());
		
		List<Product> productsInDBlist2 = productDao.findAll();
		Assert.assertTrue(productsInDBlist2.size() == 2);

		Product productTest2 = productDao.getEntityByKey(product2.getKey());
		Assert.assertNotNull(productTest2);
		
		List<StocktakeItem> items = new ArrayList<StocktakeItem>();
		
		StocktakeItem stItem1 = new StocktakeItem();
		stItem1.setStocktake(stocktake);
		stItem1.setProductKey(product1.getKey());
		stItem1.setQuantity(BigDecimal.valueOf(2.2));

		StocktakeItem stItem2 = new StocktakeItem();
		stItem2.setStocktake(stocktake);
		stItem2.setProductKey(product2.getKey());
		stItem2.setQuantity(BigDecimal.valueOf(31.5));
		
		items.add(stItem1);
		items.add(stItem2);
		
		stocktake.setItems(items);
		Assert.assertNull(stocktake.getKey());
		stocktakeDao.save(stocktake);
		Assert.assertNotNull(stocktake.getKey());
		Assert.assertNotNull(stocktake.getItems().get(0).getKey());
		Assert.assertNotNull(stocktake.getItems().get(1).getKey());
		
		Stocktake resultSt = stocktakeDao.getEntityByKey(stocktake.getKey());
		Assert.assertTrue(resultSt.getItems().size() == 2);
		for (StocktakeItem item : resultSt.getItems()) {
			Product product = productDao.getEntityByKey(item.getProductKey());
			Assert.assertNotNull(product);
			Assert.assertTrue(item.getQuantity().intValue() > 1);
		}
	}
}
