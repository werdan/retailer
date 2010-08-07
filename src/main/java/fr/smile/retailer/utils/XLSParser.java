package fr.smile.retailer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fr.smile.retailer.dao.XLSLineModelFactory;
import fr.smile.retailer.model.XLSLineModel;

public interface XLSParser {

	public List<? extends XLSLineModel> parse(InputStream in, XLSLineModelFactory modelService) throws IOException;

}