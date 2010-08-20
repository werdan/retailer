<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<h1>Отчет по средним ценам и себестоимости закупки у поставщиков в магазине ${store.name}</h1>

<table>
	<c:forEach var="row" items="${productsVersusSuppliers.rows}" varStatus="rowNumber">
		<tr>
		<c:forEach var="cell" items="${row.cells}">
			<c:choose>
				<c:when test='${rowNumber.count eq 1}'>
					<th>${cell.value}</th>
	    		</c:when>
	    		<c:otherwise>
					<td>${cell.value}</td>
	    		</c:otherwise>			
    		</c:choose>
		</c:forEach>
		</tr>
	</c:forEach>
</table>
