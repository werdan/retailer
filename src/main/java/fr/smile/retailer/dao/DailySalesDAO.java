package fr.smile.retailer.dao;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import fr.smile.retailer.dao.interfaces.IDailySalesDAO;
import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.DailySales;

public class DailySalesDAO extends AbstractAppEngineDAO<DailySales> implements IDailySalesDAO {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private IStoreDAO storeDao;

	
	@Override
	/**
	 * Returns value by date after reseting seconds to 00. There can be only one value per date (i.e. year + month + day)
	 */
	public DailySales getByDate(Date date) {
		date = DateUtils.round(date, Calendar.DAY_OF_MONTH);
		logger.debug("Getting DailySales by date: " + date);
	    Filter filter = new Filter("date == inputDate", "java.util.Date inputDate", date);
	    return this.findUniqueFiltered(filter);
	}
	
	@Override
	public void save(Object entity) {
		if (entity instanceof DailySales) {
			DailySales ds = (DailySales) entity;
			if (ds.getStore() != null) {
				storeDao.save(ds.getStore());
				ds.setStoreKey(ds.getStore().getKey());
			}
			super.save(entity);
		}
	}
	
	@Override
	public DailySales initUnownedRelations(DailySales ds) {
		if (ds.getStoreKey() != null ) {
			ds.setStore(storeDao.getEntityByKey(ds.getStoreKey()));
		} else {
			ds.setStore(null);
		}
		return ds;
	}
}