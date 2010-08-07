package fr.smile.retailer.utils;

import java.math.BigDecimal;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class CustomNumberUtilsTest extends AbstractTestNGSpringContextTests {

	@Test
	public void testCreateIntegerViaDouble() {
		Assert.assertTrue(CustomNumberUtils.createIntegerViaDouble(null) == 0);
		Assert.assertTrue(CustomNumberUtils.createIntegerViaDouble("") == 0);
		Assert.assertTrue(CustomNumberUtils.createIntegerViaDouble("1.0") == 1);
		Assert.assertTrue(CustomNumberUtils.createIntegerViaDouble("1.3") == 1);
	}

	@Test
	public void testCreateBigDecimal() {
		Assert.assertTrue(CustomNumberUtils.createBigDecimal(null).compareTo(BigDecimal.valueOf(0)) == 0);
		Assert.assertTrue(CustomNumberUtils.createBigDecimal("").compareTo(BigDecimal.valueOf(0)) == 0);
		Assert.assertTrue(CustomNumberUtils.createBigDecimal("1.0").compareTo(BigDecimal.valueOf(1.0d)) == 0);
		Assert.assertTrue(CustomNumberUtils.createBigDecimal("1.3").compareTo(BigDecimal.valueOf(1.3d)) == 0);
	}	
}
