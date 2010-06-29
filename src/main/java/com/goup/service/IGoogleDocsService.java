package com.goup.service;

import java.io.IOException;

import com.google.gdata.client.docs.DocsService;
import com.google.gdata.util.ServiceException;

public interface IGoogleDocsService {
	/**
	 * Returns document html by document category and document title
	 * 
	 * @param category
	 * @param docTitle
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 * @throws DocumentNotFound 
	 */
	public String getDocumentHtml(String categoryPath, String docTitle) throws IOException, ServiceException, DocumentNotFound;
	public void setDocService(DocsService client);
}
