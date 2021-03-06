package fr.smile.retailer.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.smile.retailer.dao.interfaces.IDailySalesDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.DailySales;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.web.propertyeditors.DateEditor;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

/**
 * Controller for Daily sales manipulation
 * </br>
 * 
 */
@Controller
public class DailySalesController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	public final static String VIEW_NAME = "dailysales";	
	public final static String MODEL_NAME = "dailysales";
		
	@Autowired
	private IDailySalesDAO dailySalesDAO;
	
	@Autowired
	private StoreEditor storePropEditor;
	@Autowired
	private IStoreDAO storeDao;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Date.class, new DateEditor());
        dataBinder.registerCustomEditor(Store.class, storePropEditor);
        
        dataBinder.setIgnoreUnknownFields(false);
    }
	
	/**
	 * Attributes of the model
	 * @return
	 */
	
	@ModelAttribute(StoreController.MODEL_NAME + "s")
	public List<Store> getStores() {
		return storeDao.findAll();
	}
	
	@ModelAttribute("alldailysales") 
	public List<DailySales> getAllDailySales() {
		return dailySalesDAO.findAll();
	}

	@ModelAttribute(MODEL_NAME)
	public DailySales getNewDailySales() {
		return new DailySales();
	}

	/**
	 * Request mappings
	 * 
	 */
	@RequestMapping(value="/forms/dailysales", method=RequestMethod.GET)
	public ModelAndView getNew(){
		return new ModelAndView(VIEW_NAME);
	}

	@RequestMapping(value="/forms/dailysales/{date}", method=RequestMethod.GET)
	@ModelAttribute(MODEL_NAME)
	public ModelAndView getByDate(@PathVariable Date date) {
		dailySalesDAO.getByDate(date);
		return new ModelAndView(VIEW_NAME);
	}
		
	@RequestMapping(value="/forms/dailysales", method=RequestMethod.POST)
	public ModelAndView submit(@ModelAttribute DailySales dailySales, BindingResult result) {
		ModelAndView mav = new ModelAndView("redirect:" + VIEW_NAME);
		dailySalesDAO.save(dailySales);
		return mav;
	}
}

