<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form commandName="delivery" action="/home/forms/delivery" enctype="multipart/form-data">
	<fieldset>	
		<label for="date">Дата:</label> 
		<form:input path="date" id="date" class="form-field" />
		<label for="store">Магазин</label>
		<form:select path="store">
			<form:option value="0" label="Select" />
			<form:options items="${stores}" itemValue="key" itemLabel="name" />
		</form:select>	

		<form:select path="supplier">
			<form:option value="0" label="Select" />
			<form:options items="${suppliers}" itemValue="key" itemLabel="name" />
		</form:select>	

		<input type="file" name="deliveryxls"/>
		<input type="submit"/>
		</fieldset>
</form:form>
