package fr.smile.retailer.web.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.api.datastore.KeyFactory.Builder;

import fr.smile.retailer.model.Store;

public class KeyEditor extends PropertyEditorSupport {
	
	public void setAsText(String text) {
		
		Pattern p = Pattern.compile("(\\w+)\\((\\d+)\\)");
		Matcher m = p.matcher(text);
		if (m.find()) {
			setValue(new Builder(m.group(1), m.group(2)).getKey());
		} 
		setValue(null);
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		if (value != null && value instanceof Store) {
			Store st = (Store) value;
			return st.getKey().getKind() + "(" + st.getKey().getId() + ")";
		}
		return "";
	}
}
