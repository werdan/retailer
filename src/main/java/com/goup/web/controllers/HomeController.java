package com.goup.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
/**
 * Basic controller for all the pages of frontend of go-up website
 * </br>
 * Returns ModelAndViews with viewNames with suffix "@google" if page content is to be fetched from Google Docs 
 * 
 */
public class HomeController {
	
	/**
	 * <p>
	 * This suffix serves as marker for Tiles definition resolving</br>
	 * </br>
	 * By default Tiles definition is described like this in WEB-INF/tiles/main.xml: </br>
	 * </br>
	 * <code>defition name="*@google" extends="page.common"</code></br>
	 * So if you change GOOGLE_SUFFIX, you have to change also main.xml definitions declaration
	 * </p>
	 */
	protected final static String GOOGLE_SUFFIX = "@google";
	
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping("/*")
	public ModelAndView getIndex() {
		logger.debug("Got request for home/index");
		return new ModelAndView("index");	
	}
	
	@RequestMapping("/{type}/{id}")
	public ModelAndView getHtmlView(@PathVariable String type, @PathVariable String id) {
		ModelAndView mav = new ModelAndView(id + GOOGLE_SUFFIX);
		mav.addObject("categoryPath",type);
		mav.addObject("documentTitle",id);
		return mav;
	}
		
	@RequestMapping("/{page}")
	public ModelAndView getStaticPage(@PathVariable String page) throws Exception {
		ModelAndView mav = new ModelAndView(page + GOOGLE_SUFFIX);
		mav.addObject("documentTitle", page);
		return mav;
	}
}
