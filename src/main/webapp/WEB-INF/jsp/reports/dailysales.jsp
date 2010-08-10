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
  <c:forEach var="storeDailySalesEntry" items="${dailySalesByStore}" varStatus="counterStore">

 	data.addColumn('number', 'Продажи ${storeDailySalesEntry.key.name}, грн');
  <c:forEach var="storeDailySalesEntry" items="${dailySalesByStore}" varStatus="counterStore">
  	data.addRows(${fn:length(storeDailySalesEntry.value)});

  	<c:forEach var="dailysale" items="${storeDailySalesEntry.value}" varStatus="counterItem">
		data.setValue(${counterItem.count-1},0,new Date("${dailysale.date}"));
		data.setValue(${counterItem.count-1},${counterStore.count},${dailysale.sum});
 	 </c:forEach>
  </c:forEach>

  var annotatedtimeline = new google.visualization.AnnotatedTimeLine(
      document.getElementById('visualization'));
  annotatedtimeline.draw(data, {'displayAnnotations': true});
}

​  </script>
  
  <div id="visualization" style="width: 800px; height: 400px;"/>

