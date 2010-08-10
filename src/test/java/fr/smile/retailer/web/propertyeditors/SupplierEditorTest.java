package fr.smile.retailer.web.propertyeditors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.interfaces.ISupplierDAO;
import fr.smile.retailer.model.Supplier;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class SupplierEditorTest extends AbstractTestNGSpringContextTests {

	private LocalServiceTestHelper helper = null;
	
	@Autowired
	private ISupplierDAO supplierDao;

	@Autowired
	private SupplierEditor supplierPropertyEditor;
	
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
		Supplier sup = new Supplier("test");
		supplierDao.save(sup);
		supplierPropertyEditor.setValue(sup);
		String stringSupplier = supplierPropertyEditor.getAsText();
		
		Supplier sup2 = new Supplier("test23");
		supplierDao.save(sup2);
		
		supplierPropertyEditor.setValue(sup2);
		supplierPropertyEditor.setAsText(stringSupplier);
		Assert.assertEquals((Supplier) supplierPropertyEditor.getValue(), sup);
	}
}
