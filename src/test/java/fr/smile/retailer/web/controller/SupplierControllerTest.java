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

import fr.smile.retailer.dao.interfaces.ISupplierDAO;
import fr.smile.retailer.model.Supplier;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class SupplierControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private SupplierController controller;

	private HandlerAdapter handlerAdapter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	private LocalServiceTestHelper helper = null;

	@Autowired
	private ISupplierDAO supplierDao;

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
		request.setRequestURI("/forms/supplier");
		request.setMethod("GET");
		ModelAndView mav = null;
		try {
			mav = handlerAdapter.handle(request, response, controller);
		} catch (Exception e) {
			Assert.fail("Expecting no exception, got: ",e);
		}
		ModelAndViewAssert.assertViewName(mav, controller.VIEW_NAME);
		ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, controller.MODEL_NAME, Supplier.class);
	}
	
	@Test
	public void testSubmit() {
			request.setRequestURI("/forms/supplier");
			request.setMethod("POST");
			request.addParameter("name", "testname");
			Assert.assertTrue(supplierDao.findAll().size() == 0);
			try {
				handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			Assert.assertTrue(supplierDao.findAll().size() == 1);
			List<Supplier> list = supplierDao.findAll();
			Supplier supplier = list.get(0);
			
			Assert.assertEquals(supplier.getName(),"testname");
	}	
}
