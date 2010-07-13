package fr.smile.retailer.web.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.IDailySalesDAO;
import fr.smile.retailer.model.DailySales;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class DailySalesControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private DailySalesController controller;

	@Autowired
	private IDailySalesDAO dailySalesDAO;
	
	private HandlerAdapter handlerAdapter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	private LocalServiceTestHelper helper = null;

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
	public void testGetNew() {
		request.setRequestURI("/forms/dailysales");
		request.setMethod("GET");
		ModelAndView mav = null;
		try {
			mav = handlerAdapter.handle(request, response, controller);
		} catch (Exception e) {
			Assert.fail("Expecting no exception, got: ",e);
		}
		ModelAndViewAssert.assertViewName(mav, DailySalesController.VIEW_NAME);
		ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, DailySalesController.MODEL_NAME, DailySales.class);
	}
	
	@Test
	public void testSubmit() {
			request.setRequestURI("/forms/dailysales");
			request.setMethod("POST");
			request.addParameter("date", "06-06-2010");
			
			request.addParameter("sum", "100");
			Assert.assertTrue(dailySalesDAO.findAll().size() == 0);
			try {
				handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			Assert.assertTrue(dailySalesDAO.findAll().size() == 1);
			List<DailySales> list = dailySalesDAO.findAll();
			DailySales ds = list.get(0);
			Assert.assertTrue(BigDecimal.valueOf(100).equals(ds.getSum()));
	}	
}
