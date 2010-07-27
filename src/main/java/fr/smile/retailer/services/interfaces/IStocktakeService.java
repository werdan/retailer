package fr.smile.retailer.services.interfaces;

import java.util.List;

import fr.smile.retailer.model.StocktakeItem;

public interface IStocktakeService {

	public StocktakeItem createItem(List<String> values);

}