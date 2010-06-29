package com.goup.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gdata.client.DocumentQuery;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ServiceException;

public class GoogleDocsService implements IGoogleDocsService {

	@Autowired
	private DocsService client;
	
	private String webContentPrefix;
	private final Pattern patternBody = Pattern.compile("<body\\b[^>]*>(.*?)</body>", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
	private final Pattern patternIE6comment = Pattern.compile("<!--.*-->", Pattern.MULTILINE + Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
	private final Logger logger = Logger.getLogger(this.getClass());
	
	
	private String getDocumentId(String category, String documentTitle) throws IOException, ServiceException, DocumentNotFound {
		// TODO add prefix-category "webcontent"
		System.out.println(this.webContentPrefix);
		// TODO Get documentId by category
		DocumentQuery query = new DocumentQuery(new URL("https://docs.google.com/feeds/default/private/full"));
		query.setTitleQuery(documentTitle);
		query.setTitleExact(true);
		query.setMaxResults(1);
		DocumentListFeed feed = client.getFeed(query, DocumentListFeed.class);
		for (DocumentListEntry document : feed.getEntries()) {
			logger.debug("Document id with title = " + documentTitle + " is " + document.getDocId());
			return document.getDocId();
		}
		throw new DocumentNotFound("Document in category:" + category + " and with title:" + documentTitle + " can not be found");
	}

	private MediaSource downloadDocumentHtml(String docId) throws IOException, ServiceException {
		MediaContent mc = new MediaContent();
		String exportUrl = "https://docs.google.com/feeds/download/documents/Export?docId=" + docId + "&exportFormat=html";
		mc.setUri(exportUrl);
		return client.getMedia(mc);
	}
	
	@Override
	public String getDocumentHtml(String categoryPath, String documentTitle) throws IOException, ServiceException, DocumentNotFound {
		logger.debug("Preparing to fetch data from document with title = " + documentTitle + " and category path = " + categoryPath );
		String docId = getDocumentId(categoryPath, documentTitle);
		//TODO: add categoryPath lookup
		MediaSource mc = downloadDocumentHtml(docId);
		InputStream inStream = mc.getInputStream();
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} finally {
			inStream.close();
		}
		logger.debug("Full document HTML:" + sb.toString());
		logger.debug("Extracting only HTML body");
	    Matcher m = patternBody.matcher(sb.toString());
	    m.find();
	    if (m.groupCount() != 1) {
	    	throw new ServiceException("Document HTML structure is broken. Expected one <body> tag, got: " + m.groupCount());
	    }
		logger.debug("Deleting <!--[if IE6]--> comments");
	    Matcher mIE6 = patternIE6comment.matcher(m.group(1));
	    String content = mIE6.replaceAll("").trim();
	    logger.debug("Clean HTML content: " + content);
	    return content;
	}

	public void setPrefix(String webContentPrefix) {
		this.webContentPrefix = webContentPrefix;
	}
	
	public void setDocService(DocsService client) {
		this.client = client;
	}
}
