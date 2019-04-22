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
<title>Item List</title>
</head>
<body>
	<%@include file="./header.jsp"%>

	<h1>Item List</h1>

	<div id="monsterFilterDiv">
		<form id="monsterFilterForm" action="filterItems">
			<table>
				<tr>
					<td>Type</td>
					<td>Min Weight</td>
					<td>Max Weight</td>
					<td>Rarity</td>
				</tr>
				<tr>
					<td><select class="monsterFilterSelect" name="type"
						multiple="multiple">
							<option value="A">Ammo</option>
							<option value="G">General</option>
							<option value="HA">Heavy Armor</option>
							<option value="LA">Light Armor</option>
							<option value="M">Melee Weapon</option>
							<option value="MA">Medium Armor</option>
							<option value="P">Potion</option>
							<option value="R">Ranged Weapon</option>
							<option value="RG">Ring</option>
							<option value="RD">Rod</option>
							<option value="SC">Scroll</option>
							<option value="S">Shield</option>
							<option value="ST">Staff</option>
							<option value="$">Valuable</option>
							<option value="WD">Wand</option>
							<option value="W">Wondrous</option>
					</select></td>
					<td><input type="number" name="minWeight" min="0" max="1000"></td>
					<td><input type="number" name="maxWeight" min="0" max="1000"></td>
					<td><select class="monsterFilterSelect" name="rarity"
						multiple="multiple">
							<option value="COMMON">Common</option>
							<option value="UNCOMMON">Uncommon</option>
							<option value="RARE">Rare</option>
							<option value="VERY RARE">Very Rare</option>
							<option value="LEGENDARY">Legendary</option>
							<option value="ARTIFACT">Artifact</option>
					</select></td>
				</tr>
				<tr>
					<td colspan="3" style="text-align: right;"><input
						type="submit" value="Submit"></td>
					<td><input type="reset" value="Reset"
						onclick="document.getElementById('resetForm').submit()"></td>
				</tr>
			</table>
		</form>
	</div>

	<form id="resetForm" action="reset"></form>

	<c:if test="${!itemList.isEmpty()}">
		<br />
		<hr />
		<br />

		<table class="encounterMakerTable">
			<tr>
				<td class="encounterMakerTableHeader"><a href="sortByName">Name</a></td>
				<td class="encounterMakerTableHeader"><a href="sortByType">Type</a></td>
				<td class="encounterMakerTableHeader"><a href="sortByWeight">Weight</a></td>
				<td class="encounterMakerTableHeader"><a href="sortByRarity">Rarity</a></td>
			</tr>
			<c:forEach var="item" items="${itemList}" varStatus="status">
				<tr>
					<td><a href="itemInfo${status.index}">${item.name}</a></td>
					<td>${item.type}</td>
					<td>${item.weight}</td>
					<td>${item.rarity}</td>
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