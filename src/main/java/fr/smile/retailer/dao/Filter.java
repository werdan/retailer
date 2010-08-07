package fr.smile.retailer.dao;


/**
 * Bean class that contains data for filtered queries
 * 
 * @author ansam
 * 
 */
public class Filter {
	private String filterExpession;
	private String paramExpression;
	private Object paramValue;
	private Object paramValue2;
	private Object paramValue3;

	/**
	 * Easy constructor that is convenient for 1 parameter. Accepts filter expression, 
	 * parameter declaration expression and parameter value
	 * 
	 * @param filterExpression
	 * @param paramExpression
	 * @param paramValue
	 */
	
	public Filter(String filterExpression, String paramExpression, Object paramValue) {
		this.filterExpession = filterExpression;
		this.paramExpression = paramExpression;
		this.paramValue = paramValue;
	}

	public Filter() {
	}

	public void setFilterExpession(String filterExpession) {
		this.filterExpession = filterExpession;
	}

	public String getFilterExpession() {
		return filterExpession;
	}

	public void setParamExpression(String paramExpression) {
		this.paramExpression = paramExpression;
	}

	public String getParamExpression() {
		return paramExpression;
	}

	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

	public Object getParamValue() {
		return paramValue;
	}

	public int getParamNumber() {
		if (this.paramValue != null) {
			if (this.getParamValue2() != null) {
				if (this.getParamValue3() != null) { return 3;} 
				else { return 2; }
			} else { return 1; }
		} else {
			return 0;
		}
	}

	public void setParamValue2(Object paramValue2) {
		this.paramValue2 = paramValue2;
	}

	public Object getParamValue2() {
		return paramValue2;
	}

	public void setParamValue3(Object paramValue3) {
		this.paramValue3 = paramValue3;
	}

	public Object getParamValue3() {
		return paramValue3;
	}

}
