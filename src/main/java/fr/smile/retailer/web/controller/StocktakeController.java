package fr.smile.retailer.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import fr.smile.retailer.dao.interfaces.IStocktakeDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.utils.SimpleXLSParser;
import fr.smile.retailer.web.propertyeditors.StoreEditor;

/**
 * Controller for Daily sales manipulation </br>
 * 
 */
@Controller
public class StocktakeController {

	public final static String VIEW_NAME = "stocktake";
	public final static String MODEL_NAME = "stocktake";

	private final Logger logger = Logger.getLogger(this.getClass());

	
	@Autowired
	public IStocktakeDAO stocktakeDao;
	@Autowired
	public StoreEditor storePropEditor;
	@Autowired
	public IStoreDAO storeDao;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(Store.class, storePropEditor);
		dataBinder.setIgnoreInvalidFields(true);
		dataBinder.setIgnoreUnknownFields(false);
	}

	/**
	 * Attributes of the model
	 * 
	 * @return
	 */

	@ModelAttribute("stores")
	public List<Store> getStores() {
		return storeDao.findAll();
	}

	@ModelAttribute(MODEL_NAME)
	public Stocktake getNewStocktake() {
		return new Stocktake();
	}

	/**
	 * Request mappings
	 * 
	 */
	@RequestMapping(value = "/forms/stocktake", method = RequestMethod.GET)
	public ModelAndView getNew() {
		return new ModelAndView(VIEW_NAME);
	}

	
	@RequestMapping(value = "/forms/stocktake", method = RequestMethod.POST)
	public String submit(@RequestParam("store") String storeKey, 
						 @RequestParam("file") MultipartFile file,
						 @RequestParam("date") String dateShort) throws IOException {
		if (!file.isEmpty()) {
			
			return "redirect:/home/index";			
		} else {
			logger.error("Error in stocktake processing: file is empty");
			return "redirect:/home/forms/stocktake";
		}
	}
}
