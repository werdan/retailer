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

import com.google.appengine.api.datastore.Blob;

import fr.smile.retailer.dao.interfaces.IStockmoveDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Stockmove;
import fr.smile.retailer.model.StockmoveItem;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.services.interfaces.IStockmoveService;
import fr.smile.retailer.utils.SimpleXLSParser;
import fr.smile.retailer.utils.XLSParser;
import fr.smile.retailer.web.propertyeditors.DateEditor;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

/**
 * Controller for Stockmove manipulation </br>
 * 
 */
@Controller
public class StockmoveController {

	public final static String VIEW_NAME = "stockmove";
	public final static String MODEL_NAME = "stockmove";

	private final Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	public IStockmoveDAO stockmoveDao;

	@Autowired
	public StoreEditor storePropEditor;

	@Autowired
	public IStoreDAO storeDao;
	
	@Autowired
	private IStockmoveService stockmoveService;

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
	public Stockmove getNewStockmove() {
		return new Stockmove();
	}

	/**
	 * Request mappings
	 * 
	 */
	@RequestMapping(value = "/forms/stockmove", method = RequestMethod.GET)
	public ModelAndView getNew() {
		return new ModelAndView(VIEW_NAME);
	}

	
	@RequestMapping(value = "/forms/stockmove", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public String submit(@RequestParam("store") Store store, 
						 @RequestParam("stockmovexls") MultipartFile stockmoveFile,
						 @RequestParam("date") Date dateShort) throws IOException {
		if (!stockmoveFile.isEmpty()) {
			logger.debug("Parsing Delivery Excel document");
			XLSParser parser = new SimpleXLSParser();
			List<StockmoveItem> stockmoveItemsList = (List<StockmoveItem>) parser.parse(stockmoveFile.getInputStream(), stockmoveService);
			Stockmove stockmove = new Stockmove();
			stockmove.setDate(dateShort);
			stockmove.setStore(store);
			stockmove.setItems(stockmoveItemsList);
			stockmove.setXLSBlob(new Blob(stockmoveFile.getBytes()));
			
			stockmoveDao.save(stockmove);
			
			return "redirect:/home/forms/stockmove";			
		} else {
			logger.error("Error in delivery processing: file is empty");
			return "redirect:/home/forms/stockmove";
		}
	}
}
