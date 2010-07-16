package fr.smile.retailer.web.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory.Builder;

import fr.smile.retailer.dao.interfaces.IStoreDAO;
import fr.smile.retailer.model.Store;

public class StoreEditor extends PropertyEditorSupport {
	
	@Autowired
	private IStoreDAO storeDao;
	
	public void setAsText(String text) {
		
		Pattern p = Pattern.compile("Store\\((\\d+)\\)");
		Matcher m = p.matcher(text);
		if (m.find()) {
			Key key = new Builder("Store", m.group(1)).getKey();
			setValue(storeDao.getEntityByKey(key));
			return;
		} 
		setValue(new Store(""));
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
