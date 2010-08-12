<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

 <script type="text/javascript">
 google.load('visualization', '1', {packages: ['annotatedtimeline']});
 google.setOnLoadCallback(drawVisualization);

  function drawVisualization() {
  var data = new google.visualization.DataTable();

  data.addColumn('date', 'Дата');
  data.addRows(${dailySalesCount});
  <c:forEach var="store" items="${stores}" varStatus="counterStoreColumn">
 	data.addColumn('number', 'Продажи ${store.name}, грн');
 	<c:forEach var="dailySalesEntry" items="${dailySalesByStore}" varStatus="counterDate">
 		<c:forEach var="storeDailySalesEntry" items="${dailySalesEntry.value}" varStatus="counterStoreSales">
 			<c:if test='${store.name == storeDailySalesEntry.key.name}'>
 				data.setValue(${counterDate.count-1},0,new Date("${dailySalesEntry.key}"));
				data.setValue(${counterDate.count-1},${counterStoreColumn.count},${storeDailySalesEntry.value});
 			</c:if>
		</c:forEach>
  	</c:forEach>
  </c:forEach>
  
  var annotatedtimeline = new google.visualization.AnnotatedTimeLine(
      document.getElementById('visualization'));
  annotatedtimeline.draw(data, {'displayAnnotations': true});
}

​  </script>
  
  <div id="visualization" style="width: 800px; height: 400px;"/>

