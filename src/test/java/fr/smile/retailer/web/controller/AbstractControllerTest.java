package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.interfaces.IDailySalesDAO;
import fr.smile.retailer.dao.interfaces.IDeliveryDAO;
import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.dao.interfaces.IStocktakeDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.dao.interfaces.ISupplierDAO;
import fr.smile.retailer.dao.interfaces.IZReportDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.Supplier;
import fr.smile.retailer.web.propertyeditors.DateEditor;
import fr.smile.retailer.web.propertyeditors.StoreEditor;
import fr.smile.retailer.web.propertyeditors.SupplierEditor;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class AbstractControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	protected IDailySalesDAO dailySalesDao;

	@Autowired
	protected IProductDAO productDao;

	@Autowired
	protected IStoreDAO storeDao;
	
	@Autowired
	protected ISupplierDAO supplierDao;

	@Autowired
	protected IDeliveryDAO deliveryDao; 

	@Autowired
	protected ResourceLoader loader;

	@Autowired
	protected IStocktakeDAO stocktakeDao; 

	@Autowired
	protected IZReportDAO zreportDao;
	
	protected HandlerAdapter handlerAdapter;
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
	
	protected LocalServiceTestHelper helper = null;

	protected DateEditor datePropEditor = new DateEditor();

	@Autowired
	protected StoreEditor storePropEditor;

	@Autowired
	protected SupplierEditor supplierPropEditor;
	
	//Controllers
	@Autowired
	private DeliveryController deliveryController;
	
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

	protected List<Product> createProducts(String[] productCodes) {
		List<Product> result = new ArrayList<Product>();
		for (String productCode: productCodes) {
	    	Product product = new Product();
	    	product.setName(productCode + " product");
	    	product.setCode(productCode);
	    	productDao.save(product);
	    	result.add(product);
		}
		return result;
	}
	
	protected ModelAndView loadDelivery(Date date, Store store, Supplier supplier, Resource resource) throws IOException {
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		
		request.setRequestURI("/forms/delivery");
		request.setMethod("POST");
		//Date
		datePropEditor.setValue(date);
		request.addParameter("date", datePropEditor.getAsText());

		storePropEditor.setValue(store);
		request.addParameter("store", storePropEditor.getAsText());
		
		supplierPropEditor.setValue(supplier);
		request.addParameter("supplier", supplierPropEditor.getAsText());

		//Delivery file
		MockMultipartFile mockFile = new MockMultipartFile("deliveryxls", resource.getInputStream());
		request.addFile(mockFile);
		
		ModelAndView mav = null;
		try {
			mav = handlerAdapter.handle(request, response, deliveryController);
		} catch (Exception e) {
			Assert.fail("Expecting no exception, got: ",e);
		}
		return mav;
	}

	protected Store createStore(String name) {
		Store store = new Store(name);
		storeDao.save(store);
		return store;
	}
	
	protected Supplier createSupplier(String name) {
		Supplier sp = new Supplier(name);
		supplierDao.save(sp);
		return sp;
	}
	
}
