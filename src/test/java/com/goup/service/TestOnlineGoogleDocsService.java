package com.goup.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import com.google.gdata.client.docs.DocsService;
import com.google.gdata.util.ServiceException;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class TestOnlineGoogleDocsService extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private IGoogleDocsService googleDocService;
	
	private DocsService client; 
	
	@BeforeTest
	public void setUp() {
//		googleDocService.setDocService(client);
	}
	

	public void testGetDocumentHtml() throws IOException, ServiceException, DocumentNotFound {
		String html = googleDocService.getDocumentHtml("testCategory","title");
		Assert.assertTrue(html.indexOf("<b>check</b>") != -1, "Got content:" + html);
	}
}
