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

import fr.smile.retailer.dao.interfaces.GenericDAO;
import fr.smile.retailer.dao.interfaces.IStocktakeDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.dao.interfaces.IZReportDAO;
import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.StocktakeItem;
import fr.smile.retailer.model.Store;
import fr.smile.retailer.model.ZReport;
import fr.smile.retailer.model.ZReportItem;
import fr.smile.retailer.services.interfaces.IStocktakeService;
import fr.smile.retailer.services.interfaces.IZReportService;
import fr.smile.retailer.utils.SimpleXLSParser;
import fr.smile.retailer.utils.XLSParser;
import fr.smile.retailer.web.propertyeditors.DateEditor;
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

	@Autowired
	private IStocktakeService stocktakeService;
	
	@Autowired
	private IZReportService zreportService;
	
	@Autowired
	private IZReportDAO zreportDao;

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
	@SuppressWarnings("unchecked")
	public String submit(@RequestParam("store") Store store, 
						 @RequestParam("stocktakexls") MultipartFile stockFile,
						 @RequestParam("zreportxls") MultipartFile zreportFile,
						 @RequestParam("date") Date dateShort) throws IOException {
		if (!stockFile.isEmpty() && !zreportFile.isEmpty()) {
			logger.debug("Parsing Stocktake Excel document");

			XLSParser parser = new SimpleXLSParser();
			List<StocktakeItem> stocktakeItemsList = (List<StocktakeItem>) parser.parse(stockFile.getInputStream(), stocktakeService);
			Stocktake take = new Stocktake();
			take.setDate(dateShort);
			take.setStore(store);
			take.setItems(stocktakeItemsList);
			take.setXLSBlob(new Blob(stockFile.getBytes()));
			
			logger.debug("Parsing ZReport Excel document");

			parser = new SimpleXLSParser();
			List<ZReportItem> zReportItemsList = (List<ZReportItem>) parser.parse(zreportFile.getInputStream(), zreportService);
			ZReport zreport = new ZReport();
			zreport.setItems(zReportItemsList);
			zreport.setXLSBlob(new Blob(zreportFile.getBytes()));
			zreportDao.save(zreport);
			
			take.setZreport(zreport);
			stocktakeService.calculateCosts(take);
			stocktakeDao.save(take);
			
			return "redirect:/home/index";			
		} else {
			logger.error("Error in stocktake processing: file is empty");
			return "redirect:/home/forms/stocktake";
		}
	}
}
