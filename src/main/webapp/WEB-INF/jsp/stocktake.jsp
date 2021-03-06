<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form commandName="stocktake" action="/home/forms/stocktake" enctype="multipart/form-data">
	<fieldset>	
		<label for="date">Дата:</label> 
		<form:input path="date" id="date" class="form-field" />
		<label for="store">Магазин</label>
		
		<form:select path="store">
			<form:option value="0" label="Select" />
			<form:options items="${stores}" itemValue="key" itemLabel="name" />
		</form:select>	
		
		<label for="stocktakexls">Файл с отчетом инвентаризации</label>
		<input type="file" name="stocktakexls"/>
		<label for="stocktakexls">Файл с Z-отчетом</label>
		<input type="file" name="zreportxls"/>
		<input type="submit"/>
		</fieldset>
</form:form>
