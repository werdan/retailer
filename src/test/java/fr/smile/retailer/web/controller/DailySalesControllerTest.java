package fr.smile.retailer.web.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.Test;

import fr.smile.retailer.model.DailySales;
import fr.smile.retailer.model.Store;

public class DailySalesControllerTest extends AbstractControllerTest {

	@Autowired
	private DailySalesController controller;

	@Test
	public void testGetNew() {
		request.setRequestURI("/forms/dailysales");
		request.setMethod("GET");
		ModelAndView mav = null;
		try {
			mav = handlerAdapter.handle(request, response, controller);
		} catch (Exception e) {
			Assert.fail("Expecting no exception, got: ",e);
		}
		ModelAndViewAssert.assertViewName(mav, DailySalesController.VIEW_NAME);
		ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, DailySalesController.MODEL_NAME, DailySales.class);
	}
	
	@Test
	public void testSubmit() {
	    	Store st = createStore("test");

			request.setRequestURI("/forms/dailysales");
			request.setMethod("POST");
			request.addParameter("date", "06-06-2010");
			storePropEditor.setValue(st);
			request.addParameter("store", storePropEditor.getAsText());
			request.addParameter("sum", "100");
			Assert.assertTrue(dailySalesDao.findAll().size() == 0);
			try {
				handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			List<DailySales> list = dailySalesDao.findAll();
			Assert.assertTrue(list.size() == 1);
			DailySales ds = list.get(0);
			
			GregorianCalendar cal = new GregorianCalendar(2010, Calendar.JUNE, 06, 00, 00, 00);
	    	Date dateSearch = cal.getTime();

			Assert.assertTrue(DateUtils.isSameDay(dateSearch,ds.getDate()));
			Assert.assertTrue(BigDecimal.valueOf(100).equals(ds.getSum()));
			Assert.assertTrue(ds.getStoreKey().equals(st.getKey()));
	}	
}
