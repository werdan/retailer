package fr.smile.retailer.web.controller.reports;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.smile.retailer.model.DailySales;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.web.controller.AbstractControllerTest;
import fr.smile.retailer.web.controller.ReportsController;

public class DailySalesReportTest extends AbstractControllerTest {

	@Autowired
	private ReportsController controller;
	
	@Test
	public void testGetDailySalesReport() throws UnsupportedEncodingException {
	    	Store st = new Store("test1");
	    	storeDao.save(st);

	    	Store st2 = new Store("test2");
	    	storeDao.save(st2);
	    	
	    	Calendar cal = new GregorianCalendar();
	    	cal.set(2009, Calendar.APRIL, 21, 15, 16, 17);
	    	Date date1 = cal.getTime();

	    	cal.set(2009, Calendar.APRIL, 22, 15, 16, 17);
	    	Date date2 = cal.getTime();

	    	DailySales ds1 = new DailySales();
	    	ds1.setDate(date1);
	    	ds1.setSum(BigDecimal.valueOf(100));
	    	ds1.setStore(st);
	    	dailySalesDao.save(ds1);

	    	DailySales ds2 = new DailySales();
	    	ds2.setDate(date2);
	    	ds2.setSum(BigDecimal.valueOf(200));
	    	ds2.setStore(st);
	    	dailySalesDao.save(ds2);

	    	DailySales ds3 = new DailySales();
	    	ds3.setDate(date1);
	    	ds3.setSum(BigDecimal.valueOf(200));
	    	ds3.setStore(st2);
	    	dailySalesDao.save(ds3);
	    	
			request.setRequestURI("/reports/dailysales");
			request.setMethod("GET");
 
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}

			@SuppressWarnings("unchecked")
			HashMap<Date,HashMap<Store,BigDecimal>> listDS = (HashMap<Date,HashMap<Store,BigDecimal>>) ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "dailySalesByStore", HashMap.class);
			Assert.assertTrue(listDS.size() == 2);
			Assert.assertTrue(listDS.get(DateUtils.round(date1, Calendar.DAY_OF_MONTH)).size() == 2);
			Assert.assertTrue(listDS.get(DateUtils.round(date1, Calendar.DAY_OF_MONTH)).get(st).compareTo(BigDecimal.valueOf(100)) == 0);
			Assert.assertTrue(listDS.get(DateUtils.round(date2, Calendar.DAY_OF_MONTH)).size() == 1);
			
	}	
	
}
