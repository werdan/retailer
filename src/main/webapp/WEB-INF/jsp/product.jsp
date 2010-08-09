<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form commandName="product" action="/home/forms/product">
	<fieldset>	
		<form:hidden id="key" path="key"/>
		<label for="code">Код</label>
		<form:input id="code" path="code"/>
		<label for="name">Название</label>
		<form:input id="name" path="name"/>
		<input type="submit"/>
		</fieldset>
</form:form>
<div>Все товары</div>
<table>
<c:forEach var="productitem" items="${products}">
        <tr>
          <td><a href="/home/forms/product/<c:out value="${productitem.key}"/>"><c:out value="${productitem.name}"/></a></td>
        </tr>
</c:forEach>
</table>