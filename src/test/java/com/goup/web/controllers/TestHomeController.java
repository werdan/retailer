package com.goup.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class TestHomeController extends AbstractTestNGSpringContextTests {

	@Autowired
	private HomeController controller;

	private HandlerAdapter handlerAdapter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	
	@BeforeTest
	public void setUp() {
		handlerAdapter = new AnnotationMethodHandlerAdapter();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	public void testHome() {
			request.setRequestURI("/");
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
			} catch (Exception e) {
				Assert.fail("Expecting no expection, got: ",e);
			}
			ModelAndViewAssert.assertViewName(mav, "index");
	}
	
	@Test
	public void testStaticPage() {
			request.setRequestURI("/aboutus");
			ModelAndView mav = null;
			try {
				mav = handlerAdapter.handle(request, response, controller);
				ModelAndViewAssert.assertViewName(mav, "aboutus@google");
			} catch (Exception e) {
				Assert.fail("Expecting no expection, got: ",e);
			}
	}
}
