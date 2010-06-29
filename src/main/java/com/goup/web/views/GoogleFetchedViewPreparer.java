package com.goup.web.views;

import org.apache.log4j.Logger;
import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.springframework.beans.factory.annotation.Autowired;

import com.goup.service.IGoogleDocsService;

public class GoogleFetchedViewPreparer implements ViewPreparer {

	@Autowired
	private IGoogleDocsService googleDocsService;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void execute(TilesRequestContext tilesContext, AttributeContext attributeContext) {
		logger.debug("Starting fetching data from google to put as Tiles attribute value");
		String categoryPath = tilesContext.getParam().get("categoryPath");
		String docTitle = tilesContext.getParam().get("documentTitle");
		String value;
		try {
			value = googleDocsService.getDocumentHtml(categoryPath, docTitle);
			attributeContext.putAttribute("fetchedFromGoogle", new Attribute(value));
			logger.debug("Successfully fetched data from Google and injected as Tiles attribute");
		} catch (Exception e) {
			logger.error("Can not set attribute fetch from Google on tiles template due to error: ", e);
		}
	}
	
	public void setGoogleDocsService(IGoogleDocsService service) {
		this.googleDocsService = service;
	}

}
