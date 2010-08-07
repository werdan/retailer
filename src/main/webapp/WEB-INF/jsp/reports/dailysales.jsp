<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

 <script type="text/javascript">
 google.load('visualization', '1', {packages: ['annotatedtimeline']});

  function drawVisualization() {
  var data = new google.visualization.DataTable();

  <c:forEach var="store" items="${stores}" varStatus="counter">
  	data.addColumn('date', 'Дата');
  	
  	data.addColumn('number', 'Продажи ${store.name}, грн');
  	data.addRows(${fn:length(dailysales)});

  	<c:forEach var="dailysale" items="${dailysales}" varStatus="counter">
		data.setValue(${counter.count-1},0,new Date("${dailysale.date}"));
		data.setValue(${counter.count-1},1,${dailysale.sum});
 	 </c:forEach>
  </c:forEach>

  var annotatedtimeline = new google.visualization.AnnotatedTimeLine(
      document.getElementById('visualization'));
  annotatedtimeline.draw(data, {'displayAnnotations': true});
}
	google.setOnLoadCallback(drawVisualization);

​  </script>
  
  <div id="visualization" style="width: 800px; height: 400px;"/>

