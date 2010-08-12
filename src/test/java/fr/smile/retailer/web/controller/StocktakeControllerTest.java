package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.AbstractAppEngineDAO;
import fr.smile.retailer.dao.StocktakeDAO;
import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.dao.interfaces.IStocktakeDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.dao.interfaces.IZReportDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.StocktakeItem;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.ZReport;
import fr.smile.retailer.model.ZReportItem;
import fr.smile.retailer.services.interfaces.IStocktakeService;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class StocktakeControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private StocktakeController controller;

	private HandlerAdapter handlerAdapter;
	private MockMultipartHttpServletRequest request;
	private MockHttpServletResponse response;
	
	private LocalServiceTestHelper helper = null;

	@Autowired
	private IStoreDAO storeDao;

	@Autowired
	private IProductDAO productDao;
	
	@Autowired
	private StoreEditor storePropertyEditor;

	@Autowired
	private IStocktakeDAO stocktakeDao; 
	
	@Autowired
	private ResourceLoader loader;

	@Autowired
	private IZReportDAO zreportDao;
	
    @AfterMethod
    public void tearDown() {
        helper.tearDown();
    }
	
	@BeforeMethod
	public void setUp() {
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setUp();
		handlerAdapter = new AnnotationMethodHandlerAdapter();
		request = new MockMultipartHttpServletRequest();
		response = new MockHttpServletResponse();
	}
		
	@Test(enabled = false)
	public void testSubmit() throws IOException {
			request.setRequestURI("/forms/stocktake");
			request.setMethod("POST");
			//Date
			request.addParameter("date", "18-07-2010");

			//Products
	    	Product pr1 = new Product();
	    	pr1.setName("test1");
	    	pr1.setCode("1");
	    	productDao.save(pr1);

	    	Product pr2 = new Product();
	    	pr2.setName("test2");
	    	pr2.setCode("3");
	    	productDao.save(pr2);
	    	
	    	Product pr3 = new Product();
	    	pr3.setName("test3");
	    	pr3.setCode("4");
	    	productDao.save(pr3);
			
			//Store
			Store store = new Store("test");
			storeDao.save(store);
			storePropertyEditor.setValue(store);
			request.addParameter("store", storePropertyEditor.getAsText());
			
			//Stocktake file
			Resource res = loader.getResource("classpath:/testfiles/Stocktake.xls");
			MockMultipartFile mockFile = new MockMultipartFile("stocktakexls", res.getInputStream());
			request.addFile(mockFile);

			//ZReport file
			Resource res2 = loader.getResource("classpath:/testfiles/ZReport.xls");
			MockMultipartFile mockFile2 = new MockMultipartFile("zreportxls", res2.getInputStream());
			request.addFile(mockFile2);
			
			Assert.assertTrue(stocktakeDao.findAll().size() == 0);
			Assert.assertTrue(zreportDao.findAll().size() == 0);
			
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			
			Assert.assertTrue(stocktakeDao.findAll().size() == 1);
			Stocktake take = stocktakeDao.findAll().get(0);
			
			Store storeGot = storeDao.getEntityByKey(take.getStoreKey());
			Assert.assertTrue(storeGot.getName().equals(store.getName()));

			Calendar cal = new GregorianCalendar(2010, Calendar.JULY, 18);
			Assert.assertTrue(DateUtils.isSameDay(take.getDate(), cal.getTime()));
			
			Assert.assertTrue(take.getItems().size() == 1);
			Assert.assertNotNull(take.getXLSBlob());
			
			Assert.assertNotNull(take.getZreportKey());
			
			ZReport zreport = zreportDao.getEntityByKey(take.getZreportKey());
			Assert.assertNotNull(zreport.getKey());
			Assert.assertTrue(zreport.getItems().size() == 1);
			Assert.assertNotNull(zreport.getXLSBlob());
			
			//Check that costs are filled-in
			for (StocktakeItem stocktakeItem: take.getItems()) {
				Assert.assertNotNull(stocktakeItem.getCost());
				Assert.assertTrue(stocktakeItem.getCost().compareTo(new BigDecimal(0)) == 1);
			}
			
			ModelAndViewAssert.assertViewName(mav, "redirect:/home/index");
	}	
}
