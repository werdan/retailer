<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form commandName="dailysales" action="/home/forms/dailysales">
	<fieldset>	
		<label for="date">Дата:</label> 
		<form:input path="date" id="date" class="form-field" />
		<label for="sum">Сумма (грн)</label>
		<form:input id="sum" path="sum"/>
		<label for="store">Магазин</label>
		<form:select path="store">
			<form:option value="0" label="Select" />
			<form:options items="${stores}" itemValue="name" itemLabel="key" />
		</form:select>	
		<input type="submit"/>
		${stores}
		</fieldset>
</form:form>