<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/static/css/dnd-style.css"/>" />
<link href="https://afeld.github.io/emoji-css/emoji.css"
	rel="stylesheet">
<title>${item.getName()}</title>
</head>
<body>

	<%@include file="./header.jsp"%>
	<br />
	<br />
	<br />

	<c:choose>
		<c:when test="${item.isMagic()}">
			<div class="itemInfoDivMagic">
		</c:when>
		<c:otherwise>
			<div class="itemInfoDiv">
		</c:otherwise>
	</c:choose>

	<h1>
		<b><a
			href="https://www.google.com/search?tbm=isch&q=D%26D+${item.getName()}"
			target="blank">${item.getName()}</a></b>
	</h1>
	<h3>${item.getRarity()}</h3>
	<p>${item.getText()}</p>
	<h3>Weight: ${item.getWeight()}</h3>
	</div>

</body>
</html>