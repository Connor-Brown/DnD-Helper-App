<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/static/css/style.css"/>" />
<link href="https://afeld.github.io/emoji-css/emoji.css" rel="stylesheet">
<title>Spell List</title>
</head>
<body>
	<%@include file="./header.jsp"%>

	<h1>Spell List Home Page</h1>

	<div id="spellFilterSection">
		<form id="leForm" action="filterSearch">
			<select class="spellFilterSelect" name="level" multiple="multiple">
				<option value="0" selected="selected">Cantrip</option>
				<%for (int i = 1; i < 10; i++) {%>
				<option value="<%=i%>"><%=i%></option>
				<%}%>
			</select>
			<select class="spellFilterSelect" name="school" multiple="multiple">
				<c:forEach items="${schools}" var="s">
					<option value="${s}" selected="selected">${s}</option>
				</c:forEach>
			</select>
			<select id="classSelect" class="spellFilterSelect" name="c" multiple="multiple">
				<c:forEach items="${classes}" var="c">
					<option value="${c}" selected="selected">${c}</option>
				</c:forEach>
			</select>
			<br/>
		</form>
		<form id="clearFormId" action="clearFilter"></form>
		
		<input class="spellFilterSectionInput" type="submit" value="Filter" onclick="document.getElementById('leForm').submit()"/>
		<input class="spellFilterSectionClear" type="submit" value="Clear Filter" onclick="document.getElementById('clearFormId').submit()"/>
			
	</div>

	<table id="encounterTreasureTable" style="clear: both;">
		<tr class="topRow">
			<td><a href="sortByName">Name</a></td>
			<td><a href="sortByLevel">Level</a></td>
			<td><a href="sortBySchool">School</a></td>
			<td><a href="sortByDuration">Duration</a></td>
			<td><a href="sortByRange">Range</a></td>
			<td><a href="sortByCastingTime">Casting Time</a></td>
			<td><a href="sortByClasses">Classes</a></td>
		</tr>
		<c:forEach items="${spellList}" var="t">
			<tr style="border-bottom: none;">
				<td class="encounterTreasureContent"style="border-bottom: none;">${t.name}</td>
				<td class="encounterTreasureContent"style="border-bottom: none;">${t.level}</td>
				<td class="encounterTreasureContent"style="border-bottom: none;">${t.school}</td>
				<td class="encounterTreasureContent"style="border-bottom: none;">${t.duration}</td>
				<td class="encounterTreasureContent"style="border-bottom: none;">${t.range}</td>
				<td class="encounterTreasureContent"style="border-bottom: none;">${t.castingTime}</td>
				<td class="encounterTreasureContent"style="border-bottom: none;">
					<c:forEach items="${t.classes}" var="c">${c}, </c:forEach>
				</td>
			</tr>
			<tr style="border-bottom: 1px solid black;">
				<td colspan="7" class="encounterTreasureContent">${t.text}</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>
