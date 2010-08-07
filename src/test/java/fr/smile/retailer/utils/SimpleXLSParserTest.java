package fr.smile.retailer.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
import fr.smile.retailer.model.ZReportItem;
import fr.smile.retailer.services.interfaces.IStocktakeService;
import fr.smile.retailer.services.interfaces.IZReportService;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class SimpleXLSParserTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ResourceLoader loader;

	@Autowired
	private IProductDAO productDao;
	
	@Autowired
	private IStocktakeService stocktakeService;
	
	private LocalServiceTestHelper helper = null;

	@Autowired
	private IZReportService zreportService;

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
	@SuppressWarnings("unchecked")
	public void testParserStocktakeXLS() throws IOException {
    	Product pr1 = new Product();
    	pr1.setName("test1");
    	pr1.setCode("1");
    	productDao.save(pr1);
    	
    	Product pr2 = new Product();
    	pr2.setName("test2");
    	pr2.setCode("2");
    	productDao.save(pr2);

		Resource res = loader.getResource("classpath:/testfiles/Stocktake.xls");

		XLSParser parser = new SimpleXLSParser();
		List<StocktakeItem> stocktakeItemsList = (List<StocktakeItem>) parser.parse(res.getInputStream(), stocktakeService);
		
		Assert.assertTrue(stocktakeItemsList.size() == 2);
		Assert.assertTrue(stocktakeItemsList.get(0).getProduct().getName().equals("test1"));
		Assert.assertTrue(stocktakeItemsList.get(0).getQuantity().compareTo(BigDecimal.valueOf(14.123d)) == 0 );
		Assert.assertTrue(stocktakeItemsList.get(1).getProduct().getName().equals("test2"));
		Assert.assertTrue(stocktakeItemsList.get(1).getQuantity().compareTo(BigDecimal.valueOf(22.112d)) == 0 );
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testNotFirstHeaderLine() throws IOException {
    	Product pr1 = new Product();
    	pr1.setName("test1");
    	pr1.setCode("1");
    	productDao.save(pr1);
    	
    	Product pr2 = new Product();
    	pr2.setName("test2");
    	pr2.setCode("2");
    	productDao.save(pr2);

		Resource res = loader.getResource("classpath:/testfiles/ZReport.xls");

		XLSParser parser = new SimpleXLSParser();
		List<ZReportItem> zreportItemsList = (List<ZReportItem>) parser.parse(res.getInputStream(), zreportService);
		
		Assert.assertTrue(zreportItemsList.size() == 2, "Expected 2 , but got " + zreportItemsList.size());
		Assert.assertTrue(zreportItemsList.get(0).getProduct().getName().equals("test1"));
		Assert.assertTrue(zreportItemsList.get(0).getQuantity().compareTo(BigDecimal.valueOf(2.51d)) == 0 );
		Assert.assertTrue(zreportItemsList.get(1).getProduct().getName().equals("test2"));
		Assert.assertTrue(zreportItemsList.get(1).getQuantity().compareTo(BigDecimal.valueOf(12.785d)) == 0 );
	}
	
}
