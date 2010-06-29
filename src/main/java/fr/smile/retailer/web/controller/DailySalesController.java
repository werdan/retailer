package fr.smile.retailer.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.smile.retailer.dao.IDailySalesDAO;
import fr.smile.retailer.model.DailySales;

@Controller
/**
 * Basic controller for all the pages of frontend of go-up website
 * </br>
 * 
 */
public class DailySalesController {
	
	private Logger logger = Logger.getLogger(this.getClass());
		
	@Autowired
	private IDailySalesDAO dailySalesDAO;
	
	@RequestMapping(value="/forms/dailysales", method=RequestMethod.POST)
	public ModelAndView submit() {
		DailySales submittedDailySales = new DailySales();
		dailySalesDAO.save(submittedDailySales);
		ModelAndView mav = new ModelAndView("dailysales");
		mav.addObject("list", dailySalesDAO.findAll());
		return mav;
	}
}
