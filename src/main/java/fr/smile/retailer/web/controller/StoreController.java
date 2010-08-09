package fr.smile.retailer.web.controller;

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

import com.google.appengine.api.datastore.Key;

import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.web.propertyeditors.KeyEditor;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

/**
 * Controller for Stores manipulation
 * </br>
 * 
 */
@Controller
public class StoreController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	public final static String VIEW_NAME = "store";	
	public final static String MODEL_NAME = "store";
	

	@Autowired
	private IStoreDAO storeDao;
	
	@Autowired
	private StoreEditor storePropEditor;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Key.class, new KeyEditor());
        dataBinder.setIgnoreUnknownFields(false);
    }
	
	@RequestMapping(value="/forms/store", method=RequestMethod.GET)
	public ModelAndView getNew(){
		return new ModelAndView(VIEW_NAME);
	}
	
	@ModelAttribute("stores")
	public List<Store> getStores() {
		return storeDao.findAll();
	}

	@ModelAttribute(MODEL_NAME)
	public Store getNewStore() {
		return new Store();
	}

	@RequestMapping(value="/forms/store/{id}", method=RequestMethod.GET)
	public ModelAndView getById(@PathVariable String id) {
		storePropEditor.setAsText(id);
		Store st = (Store) storePropEditor.getValue();
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(MODEL_NAME, st);
		return mav;
	}
		
	@RequestMapping(value="/forms/store", method=RequestMethod.POST)
	public ModelAndView save(@ModelAttribute Store store, BindingResult result) {
		storeDao.save(store);
		ModelAndView mav = new ModelAndView("redirect:" + VIEW_NAME);
		return mav;
	}
}

