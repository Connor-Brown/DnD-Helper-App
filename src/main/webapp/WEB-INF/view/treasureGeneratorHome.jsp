<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../style.css">
<link href="https://afeld.github.io/emoji-css/emoji.css" rel="stylesheet">
<title>Treasure Generator</title>
</head>
<body>
<%@include file="./header.jsp" %>

	<h1>Treasure Generator Home Page</h1>

	<div style="width: 100%; margin-bottom: 1.5rem;">
		<p>Select Party Level</p>
		<select id="levelSelect" onchange="changeLevel(event)">
			<option value="1 - 4" selected="selected">1 - 4</option>
			<option value="5 - 10">5 - 10</option>
			<option value="11 - 16">11 - 16</option>
			<option value="17 - 20">17 - 20</option>			
		</select>
	</div>

	<form action="./randomEncounterTreasure" class="treasureMainlyUsed">
		<input type="hidden" name="level" class="level" value="1 - 4" />
		<input type="submit" class="encounterButton" value="Get Random Encounter Treasure" />
	</form>

	<form action="./randomTreasureHoard" class="treasureMainlyUsed">
		<input type="hidden" name="level" class="level" value="1 - 4" />
		<input type="submit" class="encounterButton" value="Get Random Treasure Hoard" />
	</form>

	<hr />

	<h2>OR</h2>

	<div style="clear: both; width: 100%; margin-bottom: 1.5rem;">
		<p>Select number of magic items</p>
		<select id="numberSelect" onchange="changeNumber(event)">
			<option value="Surprise me" selected="selected">Surprise me</option>
			<%
				for (int i = 1; i < 21; i++) {
			%>
			<option value="<%=i%>"><%=i%></option>
			<%
				}
			%>
		</select>
	</div>
	
	<form action="./randomMundaneItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Mundane Items" />
	</form>

	<form action="./randomMagicItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Magic Items" />
	</form>

	<form action="./randomValuableItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Valuable Items" />
	</form>

	<form action="./randomCommonMagicItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Common Magic Items" />
	</form>

	<form action="./randomUncommonMagicItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Uncommon Magic Items" />
	</form>

	<form action="./randomRareMagicItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Rare Magic Items" />
	</form>

	<form action="./randomVeryRareMagicItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Vary Rare Magic Items" />
	</form>

	<form action="./randomLegendaryMagicItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Legendary Magic Items" />
	</form>

	<form action="./randomArtifactMagicItems" class="treasureButtom">
		<input type="hidden" name="number" class="number" value="Surprise me" />
		<input class="encounterButton" type="submit" value="Get Artifact Magic Items" />
	</form>

	<script>
		function changeNumber(e) {
			var list = document.getElementsByClassName("number");
			var i;
			for (i = 0; i < list.length; i++) {
				list[i].value = e.target.value;
			}
		}

		function changeLevel(e) {
			var list = document.getElementsByClassName("level");
			var i;
			for (i = 0; i < list.length; i++) {
				list[i].value = e.target.value;
			}
		}

		window.onload = function() {
			var i;
			var list = document.getElementsByClassName("number");
			var e = document.getElementById("numberSelect");
			for (i = 0; i < list.length; i++) {
				list[i].value = e.options[e.selectedIndex].value;
			}

			list = document.getElementsByClassName("level");
			e = document.getElementById("levelSelect");
			for (i = 0; i < list.length; i++) {
				list[i].value = e.options[e.selectedIndex].value;
			}
		}
	</script>

</body>
</html>
