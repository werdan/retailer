package fr.smile.retailer.web.controller.reports;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.smile.retailer.model.Product;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.Supplier;
import fr.smile.retailer.web.controller.AbstractControllerTest;
import fr.smile.retailer.web.controller.ReportsController;

public class SupplierCostsReportTest extends AbstractControllerTest {

	@Autowired
	private ReportsController controller;

	@Test
	public void testGetSupplierCosts() throws IOException{
		String[] productCodes = new String[]{"1","2","3","4","5","6","7"};
		createProducts(productCodes);

		
		Store store = createStore("test1");
		Supplier sup1 = createSupplier("testSp");
		Supplier sup2 = createSupplier("testSp_2");
		
		Resource deliveryFile = loader.getResource("classpath:/testfiles/Delivery1.xls");
		Calendar cal1 = new GregorianCalendar(2010, Calendar.AUGUST, 12);
		loadDelivery(cal1.getTime(), store, sup1, deliveryFile);
		
		Resource deliveryFile2 = loader.getResource("classpath:/testfiles/Delivery2.xls");
		Calendar cal2 = new GregorianCalendar(2010, Calendar.AUGUST, 11);
		loadDelivery(cal2.getTime(), store, sup2, deliveryFile2);

		Resource deliveryFile3 = loader.getResource("classpath:/testfiles/Delivery3.xls");
		Calendar cal3 = new GregorianCalendar(2010, Calendar.AUGUST, 10);
		loadDelivery(cal3.getTime(), store, sup1, deliveryFile3);
		
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
		
		@SuppressWarnings("unchecked")
		HashMap<Product,HashMap<Supplier,List<BigDecimal>>> listProducts = (HashMap<Product,HashMap<Supplier,List<BigDecimal>>>) ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "productsVersusSuppliers", HashMap.class);
		
		HashMap<Product,HashMap<Supplier,List<BigDecimal>>> targetListProducts = new HashMap<Product,HashMap<Supplier,List<BigDecimal>>>();
		HashMap<Supplier,List<BigDecimal>> supplierRow = new HashMap<Supplier,List<BigDecimal>>>();
		List<BigDecimal> productPC = new ArrayList<BigDecimal>();
		//Sup1 Pr=1
		productPC.add(BigDecimal.valueOf(33));
		productPC.add(BigDecimal.valueOf(33));
		productPC.add(BigDecimal.valueOf(33));
		targetListProducts.put(productDao.getByCode("1", )
	}
	
}
