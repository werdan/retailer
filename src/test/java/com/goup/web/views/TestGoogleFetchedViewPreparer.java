package com.goup.web.views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.google.gdata.util.ServiceException;
import com.goup.service.DocumentNotFound;
import com.goup.service.IGoogleDocsService;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml" })
public class TestGoogleFetchedViewPreparer extends AbstractTestNGSpringContextTests {

	@Autowired
	private GoogleFetchedViewPreparer viewPreparer;
	
	@Test
	public void testExecute() throws IOException, ServiceException, DocumentNotFound {
		
		Mockery context = new Mockery();
		final IGoogleDocsService mockDocsService= context.mock(IGoogleDocsService.class);
		final AttributeContext mockAttrContext = context.mock(AttributeContext.class);
		final TilesRequestContext mockTilesContext = context.mock(TilesRequestContext.class);
		final Map<String,String> mockParams = new HashMap<String,String>();
		mockParams.put("categoryPath", "catPath");
		mockParams.put("documentTitle", "testId");
		
		context.checking(new Expectations() {{
			atLeast(1).of(mockTilesContext).getParam();will(returnValue(mockParams));
			oneOf(mockDocsService).getDocumentHtml("catPath", "testId"); will(returnValue("Проверочный текст "));
			oneOf(mockAttrContext).putAttribute(with(equal("fetchedFromGoogle")), with(any(Attribute.class)));
		}});
		viewPreparer.setGoogleDocsService(mockDocsService);
		viewPreparer.execute(mockTilesContext, mockAttrContext);
		context.assertIsSatisfied();
	}
	
}
