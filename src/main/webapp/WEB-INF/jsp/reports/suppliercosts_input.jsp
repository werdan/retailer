<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

Отчет по средним ценам и себестоимости закупки у поставщиков в магазине 

<form action="/home/reports/suppliercosts" method="post">
	<fieldset>	
		<label for="store">Выберите магазин</label>
		<form:select path="stores" name="store" id="store">
			<form:option value="0" label="Select" />
			<form:options items="${stores}" itemValue="key" itemLabel="name" />
		</form:select>	
		<input type="submit"/>
		</fieldset>
</form>