package fr.smile.retailer.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.jdo.PersistenceManager;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.model.DailySales;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class DailySalesDAOTest extends AbstractTestNGSpringContextTests {

	@Autowired
    private IDailySalesDAO dailySalesDAO;
    
	private LocalServiceTestHelper helper = null;

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
    public void testDailySalesPersistenceManager() {
    	DailySales newdailysales = new DailySales();
    	Mockery context = new Mockery();
    	final PersistenceManagerLocator pmLocatorMock = context.mock(PersistenceManagerLocator.class);
    	final PersistenceManager pm = context.mock(PersistenceManager.class);
    	context.checking(new Expectations(){{
    		oneOf(pmLocatorMock).getPersistenceManager(); will(returnValue(pm));
    		oneOf(pm).makePersistent(with(any(DailySales.class)));
    		oneOf(pm).close();
    	}});

    	//Replace for test PersistenceLocator
    	PersistenceManagerLocator tmpLocator = dailySalesDAO.getPersistenceManagerLocator();
    	dailySalesDAO.setPersistenceManagerLocator(pmLocatorMock);
    	dailySalesDAO.save(newdailysales);
    	dailySalesDAO.setPersistenceManagerLocator(tmpLocator);
    	context.assertIsSatisfied();
    }
    
    @Test
    public void testGetByDate() {
    	Calendar cal = new GregorianCalendar();
    	cal.set(2009, Calendar.APRIL, 21, 15, 16, 17);
    	Date date1 = cal.getTime();

    	cal = new GregorianCalendar();
    	cal.set(2009, Calendar.APRIL, 22, 15, 23, 39);
    	Date date2 = cal.getTime();

    	cal = new GregorianCalendar();
    	cal.set(2009, Calendar.APRIL, 22, 15, 23, 55);
    	Date dateSearch = cal.getTime();

    	//First dailySales - don't expect it in test
    	DailySales ds1 = new DailySales();
    	ds1.setDate(date1);
    	ds1.setSum(BigDecimal.valueOf(1140));
    	dailySalesDAO.save(ds1);
    	
    	//Second item - looking for it
    	DailySales ds2 = new DailySales();
    	ds2.setDate(date2);
    	ds2.setSum(BigDecimal.valueOf(1140));
    	dailySalesDAO.save(ds2);
    	
    	//Trying to find by third dailySales
    	DailySales dsTarget = dailySalesDAO.getByDate(dateSearch);
    	Assert.assertEquals(dsTarget, ds2, "Got dailysales for date " + dsTarget.getDate() + " expecting " + ds2.getDate());
    	ds2.equals(dsTarget);
    }
    
    @Test
    public void testFindAll() {
    	Calendar cal = new GregorianCalendar();
    	cal.set(2009, Calendar.APRIL, 21, 15, 16, 17);
    	Date date1 = cal.getTime();
    	DailySales ds1 = new DailySales();
    	ds1.setDate(date1);
    	ds1.setSum(BigDecimal.valueOf(1140));
    	dailySalesDAO.save(ds1);
    	
    	cal.set(2009, Calendar.APRIL, 22, 15, 16, 17);
    	Date date2 = cal.getTime();
    	DailySales ds2 = new DailySales();
    	ds1.setDate(date2);
    	ds1.setSum(BigDecimal.valueOf(1140));
    	dailySalesDAO.save(ds2);
    	
    	Assert.assertTrue(dailySalesDAO.findAll().size() == 2);
    }

}
