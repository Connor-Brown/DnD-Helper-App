<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/static/css/style.css"/>" />
<link href="https://afeld.github.io/emoji-css/emoji.css" rel="stylesheet">
<title>Treasure List</title>
</head>
<body>
<%@include file="./header.jsp" %>

	<h1>Generated Treasure</h1>

	<table id="encounterTreasureTable">
		<tr class="topRow">
			<td><a href="sortByAmount">Amount</a></td>
			<td><a href="sortByName">Name</a></td>
			<td><a href="sortByType">Type</a></td>
			<td><a href="sortByWeight">Weight</a></td>
			<td><a href="sortByRarity">Rarity</a></td>
		</tr>
		<c:forEach items="${treasure}" var="t" varStatus="status">
			<c:choose>
				<c:when test="${t.isMagic()}">
					<tr class="magicItem">
				</c:when>
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
			<td class="encounterTreasureContent">${t.amount}</td>
			<td class="encounterTreasureContent">${t.name}</td>
			<td class="encounterTreasureContent">${t.type}</td>
			<td class="encounterTreasureContent">${t.weight}</td>
			<td class="encounterTreasureContent">${t.rarity}</td>
			</tr>
			<tr style="border-bottom: 1px solid black;">
				<td colspan="5" class="encounterTreasureContent">${t.text}</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>