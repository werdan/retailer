package fr.smile.retailer.web.controller.reports;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.Supplier;
import fr.smile.retailer.web.controller.ControllerAbstractTestNGSpringContextTests;
import fr.smile.retailer.web.controller.ReportsController;

public class SupplierCostsReportTest extends ControllerAbstractTestNGSpringContextTests {

	@Autowired
	private ReportsController controller;

	@Test
	public void testGetSupplierCosts() throws IOException{
		String[] productCodes = new String[]{"1","2","3"};
		createProducts(productCodes);

		
		Store store = createStore("test1");
		Supplier sup1 = createSupplier("testSp");
		Supplier sup2 = createSupplier("testSp_2");
		
		Resource deliveryFile = loader.getResource("classpath:/testfiles/Delivery1.xls");
		Calendar cal1 = new GregorianCalendar(2010, Calendar.AUGUST, 12);
		loadDelivery(cal1.getTime(), store, sup1, deliveryFile);
		
		Resource deliveryFile2 = loader.getResource("classpath:/testfiles/Delivery2.xls");
		Calendar cal2 = new GregorianCalendar(2010, Calendar.AUGUST, 11);
		loadDelivery(cal2.getTime(), store, sup1, deliveryFile2);

		Resource deliveryFile3 = loader.getResource("classpath:/testfiles/Delivery3.xls");
		Calendar cal3 = new GregorianCalendar(2010, Calendar.AUGUST, 10);
		loadDelivery(cal3.getTime(), store, sup2, deliveryFile3);
		
		Resource deliveryFile4 = loader.getResource("classpath:/testfiles/Delivery4.xls");
		Calendar cal4 = new GregorianCalendar(2010, Calendar.AUGUST, 14);
		loadDelivery(cal4.getTime(), store, sup2, deliveryFile4);
		
		request.setRequestURI("/reports/suppliercosts");
		request.setMethod("GET");

		ModelAndView mav = null;
		try {
			mav = handlerAdapter.handle(request, response, controller);
		} catch (Exception e) {
			Assert.fail("Expecting no exception, got: ",e);
		}
		
		HashMap<Date,HashMap<Store,BigDecimal>> listDS = (HashMap<Date,HashMap<Store,BigDecimal>>) ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "dailySalesByStore", HashMap.class);

		
	}
	
}
