package fr.smile.retailer.dao;

import javax.jdo.PersistenceManager;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import fr.smile.retailer.model.DailySales;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class DailySalesDAOTest extends AbstractTestNGSpringContextTests {

    private DailySalesDAO dailySalesDAO = new DailySalesDAO();
    
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

    	dailySalesDAO.setPersistenceManagerLocator(pmLocatorMock);
    	dailySalesDAO.save(newdailysales);
    	context.assertIsSatisfied();
    }

}
