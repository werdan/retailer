package fr.smile.retailer.dao;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class FilterTest extends AbstractTestNGSpringContextTests {

	@Test
	public void testGetParamNumber(){
		Filter filter = new Filter();
		filter.setParamValue2("test2");
		Assert.assertTrue(filter.getParamNumber() == 0);
		
		filter.setParamValue("test");
		Assert.assertTrue(filter.getParamNumber() == 2);

		filter.setParamValue3("test3");		
		Assert.assertTrue(filter.getParamNumber() == 3);
	}

	@Test
	public void testGetSingleParamNumber(){
		Filter filter = new Filter();
		filter.setParamValue("test2");
		Assert.assertTrue(filter.getParamNumber() == 1);
	}
	
}
