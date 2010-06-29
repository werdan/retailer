package com.goup.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gdata.client.DocumentQuery;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ServiceException;
import com.goup.gdata.client.SpringableDocsService;

@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml"})
public class TestGoogleDocsService extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private IGoogleDocsService googleDocService;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Test
	public void testGetDocumentHtml() throws IOException, ServiceException, DocumentNotFound {
		Mockery context = new Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);}};
		final DocsService mockClient = context.mock(SpringableDocsService.class);
		
		final DocumentListFeed mockDocumentListFeed = context.mock(DocumentListFeed.class);
		final DocumentListEntry dle = new DocumentListEntry();
		dle.setId("id");
		final List<DocumentListEntry> listDle = new ArrayList<DocumentListEntry>();
		listDle.add(dle);
		
		File file = resourceLoader.getResource("classpath:mocks/googleResponse.html").getFile();
		final MediaSource mediaSource = new MediaFileSource(file, "html");
		
		context.checking(new Expectations() {{
			oneOf(mockClient).getFeed(with(any(DocumentQuery.class)), with(any(Class.class))); will(returnValue(mockDocumentListFeed));
			oneOf(mockDocumentListFeed).getEntries(); will(returnValue(listDle));
			oneOf(mockClient).getMedia(with(any(MediaContent.class))); will(returnValue(mediaSource));
		}});

		googleDocService.setDocService(mockClient);
		String html = googleDocService.getDocumentHtml("testCategory","testHtml");
		Assert.assertTrue(html.indexOf("test colour") != -1, "Got content:" + html);
		
		context.assertIsSatisfied();
	}	
	
	@Test
	public void testCategory() {
		Assert.fail("Test category fetching. Carefull! Category may be absent, e.g /aboutus - it is page in webcontent, no category");
	}
}
