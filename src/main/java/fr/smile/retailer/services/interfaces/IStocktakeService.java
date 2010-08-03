package fr.smile.retailer.services.interfaces;

import java.util.List;

import fr.smile.retailer.dao.XLSLineModelFactory;
import fr.smile.retailer.model.Stocktake;
import fr.smile.retailer.model.StocktakeItem;

public interface IStocktakeService extends XLSLineModelFactory {

	public StocktakeItem createItem(List<String> values);

	public void calculateCosts(Stocktake take);

}