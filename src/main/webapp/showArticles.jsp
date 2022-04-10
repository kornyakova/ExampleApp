<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Articles</title>
</head>
<body>

<c:forEach items="${articles}" var="article">
	<center>${article.title}</center></br>
	<br></br>
	${article.article}</br>
	<br></br>
	Published at: ${article.publishedAt}</br>	
	News site: ${article.newsSite}</br>
	<center>------------------------------------------------------------------------------------------------------------</center>
</c:forEach>

</body>
</html>