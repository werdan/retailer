package fr.smile.retailer.web.controller;

import java.io.IOException;

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

import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Store;
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
	private StoreEditor storePropertyEditor;
	
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
			request.setRequestURI("/forms/stocktake");
			request.setMethod("POST");
			//Date
			request.addParameter("date", "18-07-2010");

			//Store
			Store store = new Store("test");
			storeDao.save(store);
			storePropertyEditor.setValue(store);
			request.addParameter("store", storePropertyEditor.getAsText());
			
			//File
			Resource res = loader.getResource("classpath:/testfiles/Stocktake.xls");
			MockMultipartFile mockFile = new MockMultipartFile("file", res.getInputStream());
			request.addFile(mockFile);
			
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			ModelAndViewAssert.assertViewName(mav, "redirect:/home/index");
	}	
}
