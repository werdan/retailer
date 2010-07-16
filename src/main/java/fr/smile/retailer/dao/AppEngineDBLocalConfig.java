package fr.smile.retailer.dao;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * Wrapper class for AppEngine LocalTestHelper that allows to initializate local DB environment depending on 
 * maven pom.xml profile
 * <br/>
 * 
 * @author ansam
 *
 */
public class AppEngineDBLocalConfig  {

	private boolean prod;
	private LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private static final ProductionEnv INSTANCE = new ProductionEnv();

	public AppEngineDBLocalConfig() {
		if (this.prod){
			INSTANCE.setProductionEnv(true);
		}
	}
	
	public void setProduction(boolean production) {
		this.prod = production;
	}
	
	public void setUp() {
		if (helper != null) {
			helper.setUp();
		}
	}
	
	public LocalServiceTestHelper getHelper() {
		return this.helper;
	}

	public void tearDown() {
		if (helper != null) {
			helper.tearDown();
		}
	}
	
	public final static boolean isProductionEnv() {
		return INSTANCE.isProductionEnv();
	}
}
