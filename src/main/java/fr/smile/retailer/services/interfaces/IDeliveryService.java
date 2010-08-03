package fr.smile.retailer.services.interfaces;

import fr.smile.retailer.dao.XLSLineModelFactory;
import fr.smile.retailer.model.Delivery;

public interface IDeliveryService extends XLSLineModelFactory {

	public void calculateCosts(Delivery delivery);

}