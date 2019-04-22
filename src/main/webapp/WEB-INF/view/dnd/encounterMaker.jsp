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
<title>Encounter Maker</title>
</head>
<body>
	<%@include file="./header.jsp"%>

	<h1>Encounter Maker</h1>

	<c:if test="${!selectedList.isEmpty()}">
		<h3>Total Exp for encounter: ${expTotal}</h3>
		<table class="encounterMakerTable">
			<tr>
				<td id="encounterMakerBlankColumn"></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByCount">Count</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByName">Name</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedBySize">Size</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByType">Type</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByAlignment">Alignment</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByAC">AC</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByHP">HP</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByCR">CR</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortSelectedByLegendary">Legendary?</a></td>
				<td></td>
			</tr>
			<c:forEach var="monster" items="${selectedList}" varStatus="status">
				<tr>
					<td>
						<form action="increaseCount">
							<input name="index" type="hidden" value="${status.index}"/>
							<input class="encounterMakerCountButton" type="Submit" value="+"/>
						</form>
						<br/>
						<form action="decreaseCount">
							<input name="index" type="hidden" value="${status.index}"/>
							<input class="encounterMakerCountButton" type="Submit" value="-"/>
						</form>
					</td>
					<td>${monster.count}</td>
					<td><a href="selectedMonsterInfo${status.index}" style="clear: both;">${monster.getMonster().name}</a></td>
					<td>${monster.getMonster().size}</td>
					<td>${monster.getMonster().type}</td>
					<td>${monster.getMonster().alignment1} ${monster.getMonster().alignment2}</td>
					<td>${monster.getMonster().ac}</td>
					<td>${monster.getMonster().averageHp}</td>
					<td>${monster.getMonster().cr}</td>
					<c:choose>
						<c:when test="${monster.getMonster().isLegendary()}">
							<td>Legendary</td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>
					</c:choose>
					<td>
						<form action="removeSelectedList${status.index}">
							<input type="submit" value="Remove">
						</form>
					</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="9" style="text-align: right;">
					<form action="clearSelectedList">
						<input type="submit" value="Clear List">
					</form>
				</td>
			</tr>
		</table>

		<br />
		<hr />
		<br />
	</c:if>

	<div id="monsterFilterDiv">
		<form id="monsterFilterForm" action="filterMonsters">
			<table>
				<tr>
					<td>Min Size</td>
					<td>Max Size</td>
					<td>Min AC</td>
					<td>Max AC</td>
					<td>Min HP</td>
					<td>Max HP</td>
					<td>Min CR</td>
					<td>Max CR</td>
					<td>Legendary?</td>
				</tr>
				<tr>
					<td><select class="monsterFilterSelect" name="minSize">
							<option value="T" selected="selected">Tiny</option>
							<option value="S">Small</option>
							<option value="M">Medium</option>
							<option value="L">Large</option>
							<option value="H">Huge</option>
							<option value="G">Gargantuan</option>
					</select></td>
					<td><select class="monsterFilterSelect" name="maxSize">
							<option value="T">Tiny</option>
							<option value="S">Small</option>
							<option value="M">Medium</option>
							<option value="L">Large</option>
							<option value="H">Huge</option>
							<option value="G" selected="selected">Gargantuan</option>
					</select></td>
					<td><input type="number" name="minAC" min="0" max="100"></td>
					<td><input type="number" name="maxAC" min="0" max="100"></td>
					<td><input type="number" name="minHP" min="0" max="10000"></td>
					<td><input type="number" name="maxHP" min="0" max="10000"></td>
					<td><input type="number" name="minCR" min="0" max="30"
						step="0.125"></td>
					<td><input type="number" name="maxCR" min="0" max="30"
						step="0.125"></td>
					<td><input type="radio" name="isLegendary" value="true">Yes
						<input type="radio" name="isLegendary" value="false">No <br />
						<input type="radio" name="isLegendary" value="Either">Either</td>
				</tr>
				<tr>
					<td colspan="7" style="text-align: right;"><input
						type="submit" value="Submit"></td>
					<td><input type="reset" value="Reset" onclick="document.getElementById('resetForm').submit()"></td>
				</tr>
			</table>
		</form>
	</div>
	
	<form id="resetForm" action="reset"></form>

	<c:if test="${!currentList.isEmpty()}">
		<br />
		<hr />
		<br />

		<table class="encounterMakerTable">
			<tr>
				<td class="encounterMakerTableHeader"><a
					href="sortCurrentByName">Name</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortCurrentBySize">Size</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortCurrentByType">Type</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortCurrentByAlignment">Alignment</a></td>
				<td class="encounterMakerTableHeader"><a href="sortCurrentByAC">AC</a></td>
				<td class="encounterMakerTableHeader"><a href="sortCurrentByHP">HP</a></td>
				<td class="encounterMakerTableHeader"><a href="sortCurrentByCR">CR</a></td>
				<td class="encounterMakerTableHeader"><a
					href="sortCurrentByLegendary">Legendary?</a></td>
				<td></td>
			</tr>
			<c:forEach var="monster" items="${currentList}" varStatus="status">
				<tr>
					<td><a href="currentMonsterInfo${status.index}">${monster.name}</a></td>
					<td>${monster.size}</td>
					<td>${monster.type}</td>
					<td>${monster.alignment1} ${monster.alignment2}</td>
					<td>${monster.ac}</td>
					<td>${monster.averageHp}</td>
					<td>${monster.cr}</td>
					<c:choose>
						<c:when test="${monster.isLegendary()}">
							<td>Legendary</td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>
					</c:choose>
					<td>
						<form action="addCurrentList${status.index}">
							<input type="submit" value="Add">
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<br />
	<br />
	<br />
	<br />

</body>
</html>