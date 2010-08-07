package fr.smile.retailer.dao;

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

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class ProductDAOTest extends AbstractTestNGSpringContextTests {

	private LocalServiceTestHelper helper = null;
	
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
	public void testProductsByKey() {
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
	}

	@Test
	public void testProductByNullKey() {
		Product product = productDao.getByCode(null);
		Assert.assertNull(product);
	}

	
}
