<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form commandName="supplier" action="/home/forms/supplier">
	<fieldset>	
		<label for="name">Название</label>
		<form:input id="name" path="name"/>
		<input type="submit"/>
		</fieldset>
</form:form>
<div>Все поставщики</div>
<table>
<c:forEach var="supplieritem" items="${suppliers}">
        <tr>
          <td><a href="/home/forms/supplier/<c:out value="${supplieritem.key}"/>"><c:out value="${supplieritem.name}"/></a></td>
        </tr>
</c:forEach>
</table>