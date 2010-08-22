package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.core.io.Resource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Stockmove;
import fr.smile.retailer.model.StockmoveItem;
import fr.smile.retailer.model.Store;

public class StockmoveControllerTest extends AbstractControllerTest {

	@Test
	public void testSubmit() throws IOException {
			Calendar cal = new GregorianCalendar(2010, Calendar.JULY, 18);
			Store store = createStore("test1");
			//file
			Resource res = loader.getResource("classpath:/testfiles/Stockmove.xls");
			
			Assert.assertTrue(stockmoveDao.findAll().size() == 0);

	    	createProducts(new String[] {"3","10","40","61","64","67","70","66","72","25","65","200","63","5","11"});

			ModelAndView mav = loadStockmove(cal.getTime(), store, res);
			
			Assert.assertTrue(stockmoveDao.findAll().size() == 1);
			Stockmove stockmove = stockmoveDao.findAll().get(0);
			
			Store storeGot = storeDao.getEntityByKey(stockmove.getStoreKey());
			Assert.assertTrue(storeGot.getName().equals(store.getName()));

			Calendar cal1 = new GregorianCalendar(2010, Calendar.JULY, 18);
			Assert.assertTrue(DateUtils.isSameDay(stockmove.getDate(), cal1.getTime()));
			
			Assert.assertTrue(stockmove.getItems().size() == 19, "Expecting 19, got " + stockmove.getItems().size());
			
			Product product70 = productDao.getByCode("70");
			Product product10 = productDao.getByCode("10");
			Product product5 = productDao.getByCode("5");
			Product product40 = productDao.getByCode("40");
			
			int verifications = 0;
			for (StockmoveItem stm: stockmove.getItems()) {
				if (stm.getOldProductKey().equals(product70.getKey()) 
						&& stm.getNewProductKey().equals(product10.getKey())) {
					Assert.assertTrue(stm.getQuantity().doubleValue() == 1.61d);
					verifications++;
				}
				if (stm.getOldProductKey().equals(product5.getKey()) 
						&& stm.getNewProductKey().equals(product40.getKey())) {
					Assert.assertTrue(stm.getQuantity().doubleValue() == 0.67d);
					verifications++;
				}
			}
			Assert.assertTrue(verifications == 2);
			Assert.assertNotNull(stockmove.getXLSBlob());
			
			ModelAndViewAssert.assertViewName(mav, "redirect:/home/forms/stockmove");
	}	
}
