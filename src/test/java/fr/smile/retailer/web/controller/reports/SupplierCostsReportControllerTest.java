package fr.smile.retailer.web.controller.reports;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.Supplier;
import fr.smile.retailer.web.controller.AbstractControllerTest;
import fr.smile.retailer.web.view.Row;
import fr.smile.retailer.web.view.Table;

public class SupplierCostsReportControllerTest extends AbstractControllerTest {

	@Autowired
	private SupplierCostsReportController controller;

	
	@Test
	public void testGetSupplierCosts() throws IOException{
		String[] productCodes = new String[]{"1","2","3","4","5","6","7"};
		createProducts(productCodes);

		
		Store store = createStore("test1");
		Store store2 = createStore("test2");
		
		Supplier sup1 = createSupplier("testSp");
		Supplier sup2 = createSupplier("testSp2");
		
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
		
		//This is not used in test - just to test that filtering by store works
		Resource deliveryFile5 = loader.getResource("classpath:/testfiles/Delivery4.xls");
		Calendar cal5 = new GregorianCalendar(2010, Calendar.AUGUST, 14);
		loadDelivery(cal5.getTime(), store2, sup2, deliveryFile5);
		
		request.setRequestURI("/reports/suppliercosts");
		storePropEditor.setValue(store);
		request.setParameter("store", storePropEditor.getAsText());
		request.setMethod("GET");

		ModelAndView mav = null;
		try {
			mav = handlerAdapter.handle(request, response, controller);
		} catch (Exception e) {
			Assert.fail("Expecting no exception, got: ",e);
		}
		
		Table table = (Table) ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, "productsVersusSuppliers", Table.class);
		Table targetTable = new Table(); 
		Row row = new Row(new String[] {"","testSp2 prices", "testSp2 costs", "testSp prices","testSp costs"});
		targetTable.addRow(row);
		row = new Row(new String[] {"1 product","33.000","39.370","39.440", "42.540"});
		targetTable.addRow(row);
		row = new Row(new String[] {"2 product","33.000","39.370","", ""});
		targetTable.addRow(row);
		row = new Row(new String[] {"7 product","33.000","39.600","", ""});
		targetTable.addRow(row);
		row = new Row(new String[] {"6 product","33.000","39.600","", ""});
		targetTable.addRow(row);
		row = new Row(new String[] {"3 product","","","38.670", "40.550"});
		targetTable.addRow(row);
		row = new Row(new String[] {"4 product","","","25.000", "26.570"});
		targetTable.addRow(row);
		Assert.assertEquals(table, targetTable);
	}
	
}
