package fr.smile.retailer.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import fr.smile.retailer.dao.AppEngineDBLocalConfig;

/**
 * Filter that initialize local DB config
 * @author ansam
 *
 */
public class LocalDBFilter implements Filter {

	private boolean productionEnvironment;
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
		if (!AppEngineDBLocalConfig.isProductionEnv()) {
			LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
			helper.setUp();
			chain.doFilter(arg0, arg1);
			helper.tearDown();
			return;
		}
		chain.doFilter(arg0, arg1);
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
