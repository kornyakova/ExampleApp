<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Article App</title>
</head>
<body>

	<form action="getArticles">
		<label for="submitB">Get all articles:</label>
  		<input type="submit" id="submitB" ><br><br>
	</form>
	<form action="getArticleById">
		<label for="articleId">Get article by Id:</label>
		<input type="text" name="id" id="articleId"></br>
		<input type="submit"></br>
	</form>
	<form action="getArticleByNewsSite">
		<label for="articleId">Get articles by news site:</label>
		<input type="text" name="news_site" id="news_site_name"></br>
		<input type="submit"></br>
	</form>
</body>
</html>