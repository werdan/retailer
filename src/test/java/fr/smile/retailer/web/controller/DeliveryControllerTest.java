package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.core.io.Resource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.smile.retailer.model.Delivery;
import fr.smile.retailer.model.DeliveryItem;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.Supplier;

public class DeliveryControllerTest extends AbstractControllerTest {

	@Test
	public void testSubmit() throws IOException {
			Calendar cal = new GregorianCalendar(2010, Calendar.JULY, 18);
			Store store = createStore("test1");
			
			//Supplier
			Supplier sp = createSupplier("test1");

			//Delivery file
			Resource res = loader.getResource("classpath:/testfiles/Delivery4.xls");
			
			Assert.assertTrue(deliveryDao.findAll().size() == 0);

			String[] productCodes = new String[]{"61","62","64","67","63"};
			createProducts(productCodes);

			ModelAndView mav = loadDelivery(cal.getTime(), store, sp, res);
			
			Assert.assertTrue(deliveryDao.findAll().size() == 1);
			Delivery delivery = deliveryDao.findAll().get(0);
			
			Store storeGot = storeDao.getEntityByKey(delivery.getStoreKey());
			Assert.assertTrue(storeGot.getName().equals(store.getName()));

			Calendar cal1 = new GregorianCalendar(2010, Calendar.JULY, 18);
			Assert.assertTrue(DateUtils.isSameDay(delivery.getDate(), cal1.getTime()));
			
			Assert.assertTrue(delivery.getItems().size() == 4, "Expecting 4, got " + delivery.getItems().size());
			
			for (DeliveryItem di: delivery.getItems()) {
				Assert.assertNotNull(di.getCost());
			}
			
			Assert.assertNotNull(delivery.getSupplierKey());
			Assert.assertNotNull(delivery.getXLSBlob());
			
			ModelAndViewAssert.assertViewName(mav, "redirect:/home/index");
	}	
}
