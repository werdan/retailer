package fr.smile.retailer.web.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.smile.retailer.dao.IDailySalesDAO;
import fr.smile.retailer.model.DailySales;

@Controller
/**
 * Controller for Daily sales manipulation
 * </br>
 * 
 */
public class DailySalesController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	public final static String VIEW_NAME = "dailysales";	
	public final static String MODEL_NAME = "dailysales";
	
	@Autowired
	private IDailySalesDAO dailySalesDAO;
	
	@RequestMapping(value="/forms/dailysales", method=RequestMethod.GET)
	public ModelAndView getNew(){
		return new ModelAndView(VIEW_NAME);
	}
	
	@ModelAttribute(MODEL_NAME)
	public DailySales getNewDailySales() {
		return new DailySales();
	}

	@RequestMapping(value="/forms/dailysales/{date}", method=RequestMethod.GET)
	@ModelAttribute(MODEL_NAME)
	public ModelAndView getByDate(@PathVariable Date date) {
		dailySalesDAO.getByDate(date);
		return new ModelAndView(VIEW_NAME);
	}
		
	@RequestMapping(value="/forms/dailysales", method=RequestMethod.POST)
	public ModelAndView submit(@ModelAttribute DailySales dailySales, BindingResult result) {
		dailySalesDAO.save(dailySales);
		ModelAndView mav = new ModelAndView("redirect:" + VIEW_NAME);
		return mav;
	}
}

