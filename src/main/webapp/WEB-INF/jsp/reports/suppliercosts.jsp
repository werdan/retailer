<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<table>
	<c:forEach var="row" items="${productsVersusSuppliers.rows}">
		<tr>
		<c:forEach var="cell" items="${row.cells}">
			<td>${cell.value}</td>
		</c:forEach>
		<tr>
	</c:forEach>
</table>
