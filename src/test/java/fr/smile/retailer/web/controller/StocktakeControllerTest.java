package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.StocktakeItem;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.ZReport;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

public class StocktakeControllerTest extends ControllerAbstractTestNGSpringContextTests {

	@Autowired
	private StocktakeController controller;

	private MockMultipartHttpServletRequest request;
	
	@BeforeMethod
	public void setUp() {
		super.setUp();
		request = new MockMultipartHttpServletRequest();
	}
	
	@Autowired
	private StoreEditor storePropertyEditor;
	
//	@Test(enabled = false)
	@Test
	public void testSubmit() throws IOException {
			request.setRequestURI("/forms/stocktake");
			request.setMethod("POST");
			//Date
			request.addParameter("date", "18-07-2010");

			String[] productCodes = new String[] {"1","3","4"};
			createProducts(productCodes);
			
			//Store
			Store store = createStore("test");
			storePropertyEditor.setValue(store);
			request.addParameter("store", storePropertyEditor.getAsText());
			
			//Stocktake file
			Resource res = loader.getResource("classpath:/testfiles/Stocktake.xls");
			MockMultipartFile mockFile = new MockMultipartFile("stocktakexls", res.getInputStream());
			request.addFile(mockFile);

			//ZReport file
			Resource res2 = loader.getResource("classpath:/testfiles/ZReport.xls");
			MockMultipartFile mockFile2 = new MockMultipartFile("zreportxls", res2.getInputStream());
			request.addFile(mockFile2);
			
			Assert.assertTrue(stocktakeDao.findAll().size() == 0);
			Assert.assertTrue(zreportDao.findAll().size() == 0);
			
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no exception, got: ",e);
			}
			
			Assert.assertTrue(stocktakeDao.findAll().size() == 1);
			Stocktake take = stocktakeDao.findAll().get(0);
			
			Store storeGot = storeDao.getEntityByKey(take.getStoreKey());
			Assert.assertTrue(storeGot.getName().equals(store.getName()));

			Calendar cal = new GregorianCalendar(2010, Calendar.JULY, 18);
			Assert.assertTrue(DateUtils.isSameDay(take.getDate(), cal.getTime()));
			
			Assert.assertTrue(take.getItems().size() == 1);
			Assert.assertNotNull(take.getXLSBlob());
			
			Assert.assertNotNull(take.getZreportKey());
			
			ZReport zreport = zreportDao.getEntityByKey(take.getZreportKey());
			Assert.assertNotNull(zreport.getKey());
			Assert.assertTrue(zreport.getItems().size() == 1);
			Assert.assertNotNull(zreport.getXLSBlob());
			
			//Check that costs are filled-in
			for (StocktakeItem stocktakeItem: take.getItems()) {
				Assert.assertNotNull(stocktakeItem.getCost());
				Assert.assertTrue(stocktakeItem.getCost().compareTo(new BigDecimal(0)) == 1);
			}
			
			ModelAndViewAssert.assertViewName(mav, "redirect:/home/index");
	}	
}
