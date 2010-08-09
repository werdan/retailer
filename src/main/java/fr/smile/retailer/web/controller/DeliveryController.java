package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import fr.smile.retailer.dao.interfaces.IDeliveryDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Delivery;
import fr.smile.retailer.model.DeliveryItem;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.services.interfaces.IDeliveryService;
import fr.smile.retailer.utils.SimpleXLSParser;
import fr.smile.retailer.utils.XLSParser;
import fr.smile.retailer.web.propertyeditors.DateEditor;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

/**
 * Controller for Delivery manipulation </br>
 * 
 */
@Controller
public class DeliveryController {

	public final static String VIEW_NAME = "delivery";
	public final static String MODEL_NAME = "delivery";

	private final Logger logger = Logger.getLogger(this.getClass());

	
	@Autowired
	public IDeliveryDAO deliveryDao;
	
	@Autowired
	public StoreEditor storePropEditor;
	
	@Autowired
	public IStoreDAO storeDao;

	@Autowired
	private IDeliveryService deliveryService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Store.class, storePropEditor);
		dataBinder.registerCustomEditor(Date.class, new DateEditor());
		dataBinder.setIgnoreInvalidFields(true);
		dataBinder.setIgnoreUnknownFields(false);
	}

	/**
	 * Attributes of the model
	 * 
	 * @return
	 */

	@ModelAttribute(StoreController.MODEL_NAME + "s")
	public List<Store> getStores() {
		return storeDao.findAll();
	}
	
	@ModelAttribute(MODEL_NAME)
	public Delivery getNewDelivery() {
		return new Delivery();
	}

	/**
	 * Request mappings
	 * 
	 */
	@RequestMapping(value = "/forms/delivery", method = RequestMethod.GET)
	public ModelAndView getNew() {
		return new ModelAndView(VIEW_NAME);
	}

	
	@RequestMapping(value = "/forms/delivery", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public String submit(@RequestParam("store") Store store, 
						 @RequestParam("deliveryxls") MultipartFile stockFile,
						 @RequestParam("date") Date dateShort) throws IOException {
		if (!stockFile.isEmpty()) {
			logger.debug("Parsing Delivery Excel document");
			XLSParser parser = new SimpleXLSParser();
			List<DeliveryItem> deliveryItemsList = (List<DeliveryItem>) parser.parse(stockFile.getInputStream(), deliveryService);
			Delivery delivery = new Delivery();
			delivery.setDate(dateShort);
			delivery.setStore(store);
			delivery.setItems(deliveryItemsList);

			deliveryDao.save(delivery);
			
			return "redirect:/home/index";			
		} else {
			logger.error("Error in delivery processing: file is empty");
			return "redirect:/home/forms/delivery";
		}
	}
}
