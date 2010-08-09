package fr.smile.retailer.web.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.datanucleus.sco.backed.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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

import fr.smile.retailer.dao.interfaces.IDailySalesDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.DailySales;
import fr.smile.retailer.model.Store;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class ReportsControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ReportsController controller;

	@Autowired
	private IDailySalesDAO dailySalesDAO;

	
	private HandlerAdapter handlerAdapter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	private LocalServiceTestHelper helper = null;

	@Autowired
	private IStoreDAO storeDao;

    @AfterMethod
    public void tearDown() {
        helper.tearDown();
    }
	
	@BeforeMethod
	public void setUp() {
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setUp();
		handlerAdapter = new AnnotationMethodHandlerAdapter();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}
	
	@Test
	public void testGetDailySalesReport() {
	    	Store st = new Store("test1");
	    	storeDao.save(st);

	    	Store st2 = new Store("test2");
	    	storeDao.save(st2);
	    	
	    	Calendar cal = new GregorianCalendar();
	    	cal.set(2009, Calendar.APRIL, 21, 15, 16, 17);
	    	Date date1 = cal.getTime();

	    	DailySales ds1 = new DailySales();
	    	ds1.setDate(date1);
	    	ds1.setSum(BigDecimal.valueOf(100));
	    	ds1.setStore(st);
	    	dailySalesDAO.save(ds1);

	    	DailySales ds2 = new DailySales();
	    	ds2.setDate(date1);
	    	ds2.setSum(BigDecimal.valueOf(200));
	    	ds2.setStore(st);
	    	dailySalesDAO.save(ds2);

	    	DailySales ds3 = new DailySales();
	    	ds3.setDate(date1);
	    	ds3.setSum(BigDecimal.valueOf(200));
	    	ds3.setStore(st2);
	    	dailySalesDAO.save(ds3);
	    	
			request.setRequestURI("/reports/dailysales");
			request.setMethod("GET");

			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}

			@SuppressWarnings("unchecked")
			HashMap<Store,List<DailySales>> listDS = (HashMap<Store,List<DailySales>>) ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "dailySalesByStore", HashMap.class);
			Assert.assertTrue(listDS.size() == 2);
			
			
	}	
}
