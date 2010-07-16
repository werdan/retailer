package fr.smile.retailer.web.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {
	private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	
	public void setAsText(String text) {
		try {
			setValue(format.parse(text));
		} catch (ParseException e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		if (value != null && value instanceof Date) {
			return format.format((Date) value);
		} 
		return "";
	}
}
