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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class DailySalesControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private DailySalesController controller;

	private HandlerAdapter handlerAdapter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());


    @AfterTest
    public void tearDown() {
        helper.tearDown();
    }
	
	@BeforeTest
	public void setUp() {
		helper.setUp();
		handlerAdapter = new AnnotationMethodHandlerAdapter();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void testSubmit() {
			request.setRequestURI("/forms/dailysales");
			request.setMethod("POST");
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			ModelAndViewAssert.assertViewName(mav, "dailysales");
			ModelAndViewAssert.assertModelAttributeAvailable(mav, "list");
			List list = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "list", List.class);
			Assert.assertTrue(list.size() == 1, "List contains " + list.size() + " items, expecting 1");
	}	
}
