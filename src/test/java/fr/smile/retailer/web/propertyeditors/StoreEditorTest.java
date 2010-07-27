package fr.smile.retailer.web.propertyeditors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Store;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class StoreEditorTest extends AbstractTestNGSpringContextTests {

	private LocalServiceTestHelper helper = null;
	
	@Autowired
	private IStoreDAO storeDao;

	@Autowired
	private StoreEditor storePropertyEditor;
	
    @AfterMethod
    public void tearDown() {
        helper.tearDown();
    }
	
	@BeforeMethod
	public void setUp() {
		helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
		helper.setUp();
	}


	@Test
	public void testSetAsText() {
		Store store = new Store("tests");
		storeDao.save(store);
		storePropertyEditor.setValue(store);
		String stringStore = storePropertyEditor.getAsText();
		
		Store store2 = new Store("test23");
		storeDao.save(store2);
		
		storePropertyEditor.setValue(store2);
		storePropertyEditor.setAsText(stringStore);
		Assert.assertEquals((Store) storePropertyEditor.getValue(), store);
	}
	
}
