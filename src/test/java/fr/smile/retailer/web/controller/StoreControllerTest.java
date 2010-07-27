package fr.smile.retailer.web.controller;

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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class StoreControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private StoresController controller;

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
	public void testGetNew() {
		request.setRequestURI("/forms/store");
		request.setMethod("GET");
		ModelAndView mav = null;
		try {
			mav = handlerAdapter.handle(request, response, controller);
		} catch (Exception e) {
			Assert.fail("Expecting no exception, got: ",e);
		}
		ModelAndViewAssert.assertViewName(mav, controller.VIEW_NAME);
		ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, controller.MODEL_NAME, Store.class);
	}
	
	@Test
	public void testSubmit() {
			request.setRequestURI("/forms/store");
			request.setMethod("POST");
			request.addParameter("name", "testname");
			Assert.assertTrue(storeDao.findAll().size() == 0);
			try {
				handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			Assert.assertTrue(storeDao.findAll().size() == 1);
			List<Store> list = storeDao.findAll();
			Store store = list.get(0);
			
			Assert.assertEquals(store.getName(),"testname");
	}	
}
