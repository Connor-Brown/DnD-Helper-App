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
<title>Create A New Item</title>
</head>
<body>

	<%@include file="./header.jsp"%>

	<h1>Create A New Item</h1>

	<form action="createItem" method="POST">
		<table class="creationTable">
			<tr>
				<td>Name</td>
				<td><input name="name" /></td>
			</tr>
			<tr>
				<td>Type</td>
				<td><select name="type">
						<option value="HA">Heavy Armor</option>
						<option value="LA">Light Armor</option>
						<option value="M">Melee Weapon</option>
						<option value="MA">Medium Armor</option>
						<option value="R">Ranged Weapon</option>
						<option value="A">Ammunition</option>
						<option value="S">Shield</option>
						<option value="P">Potion</option>
						<option value="RD">Rod</option>
						<option value="RG">Ring</option>
						<option value="SC">Scroll</option>
						<option value="ST">Staff</option>
						<option value="W">Wondrous Item</option>
						<option value="WD">Wand</option>
						<option value="G">General Item</option>
						<option value="$">Money</option>
				</select></td>
			</tr>
			<tr>
				<td>Weight</td>
				<td><input name="weight" type="number" value="0" step="0.1"
					min="0" max="2000" /></td>
			</tr>
			<tr>
				<td>Rarity</td>
				<td><select name="rarity">
						<option value="COMMON">COMMON</option>
						<option value="UNCOMMON">UNCOMMON</option>
						<option value="RARE">RARE</option>
						<option value="VERY RARE">VERY RARE</option>
						<option value="LEGENDARY">LEGENDARY</option>
						<option value="ARTIFACT">ARTIFACT</option>
				</select></td>
			</tr>
			<tr>
				<td>Text</td>
				<td><textarea name="text" rows="8" cols="100" id="newItemText"></textarea></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: right;"><input type="submit" value="Submit"/></td>
			</tr>
		</table>
	</form>

</body>
</html>