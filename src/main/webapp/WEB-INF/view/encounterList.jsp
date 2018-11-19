<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/static/css/style.css"/>" />
<link href="https://afeld.github.io/emoji-css/emoji.css" rel="stylesheet">
<title>Encounter List</title>
</head>
<body>
<%@include file="./header.jsp" %>

	<h1>Generated Encounter</h1>
	<h2>Level: ${level}</h2>
	<h2>Number of Players: ${number}</h2>

	<table id="encounterTreasureTable">
		<tr class="topRow">
			<td><a href="sortByCR">CR</a></td>
			<td><a href="sortByName">Name</a></td>
			<td><a href="sortByType">Type</a></td>
			<td><a href="sortByAC">AC</a></td>
			<td><a href="sortByHP">HP</a></td>
		</tr>
		<c:forEach items="${monsterList}" var="monster">
			<c:choose>
				<c:when test="${monster.isLegendary()}">
					<tr class="legendaryMonster">
				</c:when>
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
			<td class="encounterTreasureContent">${monster.cr}</td>
			<td class="encounterTreasureContent">${monster.name}</td>
			<td class="encounterTreasureContent">${monster.type}</td>
			<td class="encounterTreasureContent">${monster.ac}</td>
			<td class="encounterTreasureContent">${monster.actualHp}</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>