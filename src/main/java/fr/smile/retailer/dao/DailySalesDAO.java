package fr.smile.retailer.dao;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import fr.smile.retailer.model.DailySales;

public class DailySalesDAO extends AbstractAppEngineDAO<DailySales> implements IDailySalesDAO {

	private Logger logger = Logger.getLogger(this.getClass());

	
	@Override
	@SuppressWarnings("unchecked")
	/**
	 * Returns value by date after reseting seconds to 00. There can be only one value per date (i.e. year + month + day)
	 */
	public DailySales getByDate(Date date) {
		date = DailySales.resetHoursMinutesSeconds(date);
		logger.debug("Getting DailySales by date: " + date);
	    PersistenceManager pm = getPersistenceManagerLocator().getPersistenceManager();
	    Query query = pm.newQuery(modelClass);
	    query.setFilter("date == inputDate");
	    query.declareParameters("java.util.Date inputDate");
		try {
			List<DailySales> list = (List<DailySales>) query.execute(date);
			if (list.size() != 1) {
				logger.error("There should be only one instance of DailySales for each date, got : " + list.size());
				throw new IllegalStateException("There should be only one instance of DailySales for each date, got : " + list.size());
			}
			return list.get(0);
        } finally {
            query.closeAll();
        }

	}
}
