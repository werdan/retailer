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
import fr.smile.retailer.dao.interfaces.ISupplierDAO;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.Supplier;
import fr.smile.retailer.web.propertyeditors.KeyEditor;
import fr.smile.retailer.web.propertyeditors.StoreEditor;
import fr.smile.retailer.web.propertyeditors.SupplierEditor;

/**
 * Controller for Stores manipulation
 * </br>
 * 
 */
@Controller
public class SupplierController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	public final static String VIEW_NAME = "supplier";	
	public final static String MODEL_NAME = "supplier";
	

	@Autowired
	private ISupplierDAO supplierDao;
	
	@Autowired
	private SupplierEditor supplierPropEditor;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Key.class, new KeyEditor());
        dataBinder.setIgnoreUnknownFields(false);
    }
	
	@RequestMapping(value="/forms/supplier", method=RequestMethod.GET)
	public ModelAndView getNew(){
		return new ModelAndView(VIEW_NAME);
	}
	
	@ModelAttribute("suppliers")
	public List<Supplier> getSuppliers() {
		return supplierDao.findAll();
	}

	@ModelAttribute(MODEL_NAME)
	public Supplier getNewSupplier() {
		return new Supplier();
	}

	@RequestMapping(value="/forms/supplier/{id}", method=RequestMethod.GET)
	public ModelAndView getById(@PathVariable String id) {
		supplierPropEditor.setAsText(id);
		Store st = (Store) supplierPropEditor.getValue();
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(MODEL_NAME, st);
		return mav;
	}
		
	@RequestMapping(value="/forms/supplier", method=RequestMethod.POST)
	public ModelAndView save(@ModelAttribute Supplier supplier, BindingResult result) {
		supplierDao.save(supplier);
		ModelAndView mav = new ModelAndView("redirect:" + VIEW_NAME);
		return mav;
	}
}

