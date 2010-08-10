package fr.smile.retailer.web.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.smile.retailer.dao.Filter;
import fr.smile.retailer.dao.interfaces.IDailySalesDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.DailySales;
import fr.smile.retailer.model.Store;

/**
 * Controller for different reports </br>
 * 
 */
@Controller
public class ReportsController {

	private Logger logger = Logger.getLogger(this.getClass());

	public final static String REPORTS_PREFIX = "reports/";
	
	@Autowired
	private IDailySalesDAO dailySalesDAO;

	@Autowired
	private IStoreDAO storeDao;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setIgnoreUnknownFields(false);
	}

	
	
	@RequestMapping(value = "/reports/dailysales", method = RequestMethod.GET)
	public ModelAndView getDailySalesReport() {
		ModelAndView mav = new ModelAndView(REPORTS_PREFIX + DailySalesController.VIEW_NAME);
		mav.addObject(StoreController.MODEL_NAME + "s", storeDao.findAll());
		
		Map<Date,Map<Store,BigDecimal>> listDS = new HashMap<Date,Map<Store,BigDecimal>>();
		for (DailySales ds: dailySalesDAO.findAll()) {
			Map<Store,BigDecimal> storeSalesMap;
			if (listDS.containsKey(ds.getDate())) {
				storeSalesMap = listDS.get(ds.getDate());
			} else {
				storeSalesMap = new HashMap<Store,BigDecimal>();
			}
			storeSalesMap.put(ds.getStore(), ds.getSum());
			listDS.put(ds.getDate(), storeSalesMap);
		}
		mav.addObject("dailySalesByStore", listDS);
		return mav;
	}
}
