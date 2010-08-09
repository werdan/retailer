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

import fr.smile.retailer.dao.interfaces.IProductDAO;
import fr.smile.retailer.model.Product;

/**
 * Controller for Products manipulation
 * </br>
 * 
 */
@Controller
public class ProductController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	public final static String VIEW_NAME = "product";	
	public final static String MODEL_NAME = "product";
	

	@Autowired
	private IProductDAO productDao;
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setIgnoreUnknownFields(false);
    }
	
	@RequestMapping(value="/forms/product", method=RequestMethod.GET)
	public ModelAndView getNew(){
		return new ModelAndView(VIEW_NAME);
	}
	
	@ModelAttribute("products")
	public List<Product> getProducts() {
		return productDao.findAll();
	}

	@ModelAttribute(MODEL_NAME)
	public Product getNewProduct() {
		return new Product();
	}

	@RequestMapping(value="/forms/product/{id}", method=RequestMethod.GET)
	public ModelAndView getById(@PathVariable String id) {
		Product product = productDao.getByCode(id);
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(MODEL_NAME, product);
		return mav;
	}
		
	@RequestMapping(value="/forms/product", method=RequestMethod.POST)
	public ModelAndView save(@ModelAttribute Product product, BindingResult result) {
		productDao.save(product);
		ModelAndView mav = new ModelAndView("redirect:" + VIEW_NAME);
		return mav;
	}
}

