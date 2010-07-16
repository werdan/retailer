package fr.smile.retailer.web.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
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

import fr.smile.retailer.dao.AppEngineDBLocalConfig;
import fr.smile.retailer.dao.interfaces.IDailySalesDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.DailySales;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class DailySalesControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private DailySalesController controller;

	@Autowired
	private IDailySalesDAO dailySalesDAO;

	@Autowired
	private AppEngineDBLocalConfig helper;
	
	private HandlerAdapter handlerAdapter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	@Autowired
	private IStoreDAO storeDao;

    @AfterMethod
    public void tearDown() {
        helper.tearDown();
    }
	
	@BeforeMethod
	public void setUp() {
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
	    	Store st = new Store("test");
	    	storeDao.save(st);

			request.setRequestURI("/forms/dailysales");
			request.setMethod("POST");
			request.addParameter("date", "06-06-2010");
			StoreEditor se = new StoreEditor();
			se.setValue(st);
			request.addParameter("store", se.getAsText());
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
			
			GregorianCalendar cal = new GregorianCalendar(2010, Calendar.JUNE, 06, 00, 00, 00);
	    	Date dateSearch = cal.getTime();

			Assert.assertTrue(DateUtils.isSameDay(dateSearch,ds.getDate()));
			Assert.assertTrue(BigDecimal.valueOf(100).equals(ds.getSum()));
			Assert.assertTrue(ds.getStoreKey().equals(st.getKey()));
	}	
}
