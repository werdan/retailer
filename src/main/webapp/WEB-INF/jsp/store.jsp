<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form commandName="store" action="/home/forms/store">
	<fieldset>	
		<form:hidden id="key" path="key"/>
		<label for="name">Название</label>
		<form:input id="name" path="name"/>
		<input type="submit"/>
		</fieldset>
</form:form>
<div>Все магазины</div>
<table>
<c:forEach var="storeitem" items="${stores}">
        <tr>
          <td><a href="/home/forms/store/<c:out value="${storeitem.key}"/>"><c:out value="${storeitem.name}"/></a></td>
        </tr>
</c:forEach>
</table>