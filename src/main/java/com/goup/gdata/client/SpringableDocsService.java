package com.goup.gdata.client;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.google.gdata.client.docs.DocsService;

/**
 * Wrapper class that is intended for setting properties on DocsService.
 * Extended class accepts user credentials only in method with two arguments that can't be used with
 * Spring IoC.
 * 
 * This class accepts two separate properties and call setUserCredentials method when it is fully constructed
 * 
 * @author ansam
 *
 */
public class SpringableDocsService extends DocsService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	protected String userName;
	protected String password;

	public SpringableDocsService(String applicationName) {
		super(applicationName);
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
		
	@PostConstruct
	public void setUserCredentials() throws Exception {
		logger.info("Trying to authenticate to Google for user : " + this.userName + " and password : " + this.password);
		setUserCredentials(this.userName, this.password);
	}

}
