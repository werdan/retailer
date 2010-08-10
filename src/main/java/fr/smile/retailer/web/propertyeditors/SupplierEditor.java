package fr.smile.retailer.web.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory.Builder;

import fr.smile.retailer.dao.interfaces.ISupplierDAO;
import fr.smile.retailer.model.Supplier;

public class SupplierEditor extends PropertyEditorSupport {
	
	@Autowired
	private ISupplierDAO supplierDao;
	
	public void setAsText(String text) {
		
		Pattern p = Pattern.compile("Supplier\\((\\d+)\\)");
		Matcher m = p.matcher(text);
		if (m.find()) {
			Key key = new Builder("Supplier", new Long(m.group(1))).getKey();
			setValue(supplierDao.getEntityByKey(key));
			return;
		} 
		setValue(new Supplier(""));
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		if (value != null && value instanceof Supplier) {
			Supplier sup = (Supplier) value;
			return sup.getKey().getKind() + "(" + sup.getKey().getId() + ")";
		}
		return "";
	}
}
