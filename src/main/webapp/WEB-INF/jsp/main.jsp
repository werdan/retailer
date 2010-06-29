<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
<head>
   <title></title>
   <meta http-equiv="Content-Type" content="text/html;charset=utf-8" /> 
   <meta name="language" content="ru" /> 
   <meta name="description" content=""/> 
   <meta name="keywords" content=""/> 
   <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/> 
   <link href="/css/reset-min.css" media="screen" rel="stylesheet" type="text/css" /> 
   <link href="/css/main.css" media="screen" rel="stylesheet" type="text/css" /> 
   <script type="text/javascript" src="/js/jquery-1.4.2.min.js"></script>
   <script type="text/javascript" src="/js/main.js"></script>
</head>
<body>
    <tiles:insertAttribute name="header" />
    <tiles:insertAttribute name="menu" />
    <tiles:insertAttribute name="content" />
    <tiles:insertAttribute name="footer" />
</body>
</html>
              