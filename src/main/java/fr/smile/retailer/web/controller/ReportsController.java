package fr.smile.retailer.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.smile.retailer.dao.interfaces.IDailySalesDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;

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
		mav.addObject(StoresController.MODEL_NAME, storeDao.findAll());
		mav.addObject(DailySalesController.MODEL_NAME, dailySalesDAO.findAll());
		return mav;
	}
}
