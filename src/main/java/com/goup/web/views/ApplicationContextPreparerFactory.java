package com.goup.web.views;

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerFactory;
import org.apache.tiles.preparer.ViewPreparer;

public class ApplicationContextPreparerFactory implements PreparerFactory {

	@Override
	public ViewPreparer getPreparer(String name, TilesRequestContext context) {
		return null;
	}

}
