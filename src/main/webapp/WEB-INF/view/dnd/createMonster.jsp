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
<title>Create A New Monster</title>
</head>
<body>

	<%@include file="./header.jsp"%>

	<h1>Create A New Monster</h1>

	<form action="createMonster" method="POST">
		<table class="creationTable">
			<tr>
				<td>Name</td>
				<td><input name="name" /></td>
			</tr>
			<tr>
				<td>Size</td>
				<td><select name="size">
						<option value="G">Gargantuan</option>
						<option value="H">Huge</option>
						<option value="L">Large</option>
						<option value="M">Medium</option>
						<option value="S">Small</option>
						<option value="T">Tiny</option>
				</select></td>
			</tr>
			<tr>
				<td>Type</td>
				<td><select name="type">
						<option value="aberration">Aberration</option>
						<option value="beast">Beast</option>
						<option value="celestial">Celestial</option>
						<option value="construct">Construct</option>
						<option value="dragon">Dragon</option>
						<option value="elemental">Elemental</option>
						<option value="fey">Fey</option>
						<option value="fiend">Fiend</option>
						<option value="giant">Giant</option>
						<option value="humanoid">Humanoid</option>
						<option value="monstrosity">Monstrosity</option>
						<option value="ooze">Ooze</option>
						<option value="plant">Plant</option>
						<option value="swarm">Swarm</option>
						<option value="undead">Undead</option>
				</select></td>
			</tr>
			<tr>
				<td>Alignment</td>
				<td><select name="alignment">
						<option value="lawful good">Lawful Good</option>
						<option value="neutral good">Neutral Good</option>
						<option value="chaotic good">Chaotic Good</option>
						<option value="lawful neutral">Lawful Neutral</option>
						<option value="neutral neutral">True Neutral</option>
						<option value="chaotic neutral">Chaotic Neutral</option>
						<option value="lawful evil">Lawful Evil</option>
						<option value="neutral evil">Neutral Evil</option>
						<option value="chaotic evil">Chaotic Evil</option>
						<option value="any alignment">Any Alignment</option>
						<option value="any evil">Any Evil</option>
						<option value="any chaotic">Any Chaotic</option>
						<option value="any non-good">Any non-good</option>
						<option value="any non-lawful">Any non-lawful</option>
				</select></td>
			</tr>
			<tr>
				<td>AC</td>
				<td><input name="ac" type="number" step="1" min="0" max="100" value="10"/></td>
			</tr>
			<tr>
				<td>Average HP</td>
				<td><input name="averagehp" type="number" step="1" min="0" max="10000" value="0"/></td>
			</tr>
			<tr>
				<td>Number of Dice</td>
				<td><input name="numdice" type="number" step="1" min="0" max="1000" value="0"/></td>
			</tr>
			<tr>
				<td>Dice Size</td>
				<td><input name="dicesize" type="number" step="1" min="0" max="100" value="0"/></td>
			</tr>
			<tr>
				<td>Bonus HP</td>
				<td><input name="bonushp" type="number" step="1" min="0" max="10000" value="0"/></td>
			</tr>
			<tr>
				<td>Challenge Rating</td>
				<td><input name="cr" type="number" step=".125" min="0" max="50" value="0"></td>
			</tr>
			<tr>
				<td>Legendary?</td>
				<td><input type="checkbox" name="islegendary" value="true"/>Legendary Monster</td>
			</tr>
			<tr>
				<td>Text</td>
				<td><textarea name="text" rows="8" cols="100" id="newItemText"></textarea></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: right;"><input type="submit"
					value="Submit" /></td>
			</tr>
		</table>
	</form>

</body>
</html>