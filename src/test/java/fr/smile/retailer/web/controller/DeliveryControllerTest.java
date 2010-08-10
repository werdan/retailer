package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import fr.smile.retailer.web.propertyeditors.SupplierEditor;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class DeliveryControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private DeliveryController controller;

	private HandlerAdapter handlerAdapter;
	private MockMultipartHttpServletRequest request;
	private MockHttpServletResponse response;
	
	private LocalServiceTestHelper helper = null;

	@Autowired
	private IStoreDAO storeDao;

	@Autowired
	private IProductDAO productDao;

	@Autowired
	private ISupplierDAO supplierDao;

	@Autowired
	private StoreEditor storePropertyEditor;

	@Autowired
	private SupplierEditor supplierPropertyEditor;

	@Autowired
	private IDeliveryDAO deliveryDao; 
	
	@Autowired
	private ResourceLoader loader;

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
		
	@Test
	public void testSubmit() throws IOException {
			request.setRequestURI("/forms/delivery");
			request.setMethod("POST");
			//Date
			request.addParameter("date", "18-07-2010");

			//Store
			Store store = new Store("test");
			storeDao.save(store);
			storePropertyEditor.setValue(store);
			request.addParameter("store", storePropertyEditor.getAsText());
			
			//Supplier
			Supplier sp = new Supplier("testSup");
			supplierDao.save(sp);
			supplierPropertyEditor.setValue(sp);
			request.addParameter("supplier", supplierPropertyEditor.getAsText());

			//Delivery file
			Resource res = loader.getResource("classpath:/testfiles/Delivery.xls");
			MockMultipartFile mockFile = new MockMultipartFile("deliveryxls", res.getInputStream());
			request.addFile(mockFile);
			
			Assert.assertTrue(deliveryDao.findAll().size() == 0);

			//Products
	    	Product pr1 = new Product();
	    	pr1.setName("test1");
	    	pr1.setCode("61");
	    	productDao.save(pr1);
	    	
	    	Product pr2 = new Product();
	    	pr2.setName("test2");
	    	pr2.setCode("62");
	    	productDao.save(pr2);
	    	
	    	Product pr3 = new Product();
	    	pr3.setName("test3");
	    	pr3.setCode("64");
	    	productDao.save(pr3);

	    	Product pr4 = new Product();
	    	pr4.setName("test4");
	    	pr4.setCode("67");
	    	productDao.save(pr4);

	    	Product pr5 = new Product();
	    	pr5.setName("test5");
	    	pr5.setCode("63");
	    	productDao.save(pr5);
	    	
	    	
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			
			Assert.assertTrue(deliveryDao.findAll().size() == 1);
			Delivery delivery = deliveryDao.findAll().get(0);
			
			Store storeGot = storeDao.getEntityByKey(delivery.getStoreKey());
			Assert.assertTrue(storeGot.getName().equals(store.getName()));

			Calendar cal = new GregorianCalendar(2010, Calendar.JULY, 18);
			Assert.assertTrue(DateUtils.isSameDay(delivery.getDate(), cal.getTime()));
			
			Assert.assertTrue(delivery.getItems().size() == 4, "Expecting 4, got " + delivery.getItems().size());
			
			for (DeliveryItem di: delivery.getItems()) {
				Assert.assertNotNull(di.getCost());
			}
			
			Assert.assertNotNull(delivery.getSupplierKey());
			Assert.assertNotNull(delivery.getXLSBlob());
			
			ModelAndViewAssert.assertViewName(mav, "redirect:/home/index");
	}	
}
