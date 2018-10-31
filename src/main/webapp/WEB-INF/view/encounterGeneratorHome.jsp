<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../style.css">
<link href="https://afeld.github.io/emoji-css/emoji.css"
	rel="stylesheet">
<title>Encounter Generator</title>
</head>
<body>
	<%@include file="./header.jsp"%>

	<h1>Encounter Generator Home Page</h1>
	<div class="encounterHomeSelects">
		<p>Select the player level</p>
		<select id="levelSelect" onchange="changeLevel(event)">
			<option value="1" selected="selected">1</option>
			<%
				for (int i = 2; i < 21; i++) {
			%>
			<option value="<%=i%>"><%=i%></option>
			<%
				}
			%>
		</select>
	</div>

	<div class="encounterHomeSelects">
		<p>Select the number of players</p>
		<select id="numberSelect" onchange="changeNumber(event)">
			<option value="1" selected="selected">1</option>
			<%
				for (int i = 2; i < 11; i++) {
			%>
			<option value="<%=i%>"><%=i%></option>
			<%
				}
			%>
		</select>
	</div>

	<form action="./givenThematicEncounter" style="width: 100%;">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <select
			name="type" class="givenThematicSelect">
			<c:forEach items="${monsterTypes}" var="type">
				<option value="${type}">${type}</option>
			</c:forEach>
		</select> <input type="submit" class="givenThematicButton"
			value="Given Thematic Encounter" />
	</form>

	<hr style="clear: both; margin-bottom: 2rem;" />

	<form action="./randomThematicEncounter" class="encounterButtonTop">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Thematic Encounter" />
	</form>

	<form action="./randomEasyThematicEncounter" class="encounterButtonTop">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Easy Thematic Encounter" />
	</form>

	<form action="./randomMediumThematicEncounter"
		class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Medium Thematic Encounter" />
	</form>

	<form action="./randomHardThematicEncounter"
		class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Hard Thematic Encounter" />
	</form>

	<form action="./randomDeadlyThematicEncounter"
		class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Deadly Thematic Encounter" />
	</form>

	<hr style="clear: both; margin-bottom: 2rem;" />

	<form action="./randomSoloEncounter" class="encounterButtonTop"
		style="clear: both;">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton" value="Random Solo Encounter" />
	</form>

	<form action="./randomEasySoloEncounter" class="encounterButtonTop">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Easy Solo Encounter" />
	</form>

	<form action="./randomMediumSoloEncounter"
		class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Medium Solo Encounter" />
	</form>

	<form action="./randomHardSoloEncounter" class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Hard Solo Encounter" />
	</form>

	<form action="./randomDeadlySoloEncounter"
		class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton"
			value="Random Deadly Solo Encounter" />
	</form>

	<hr style="clear: both; margin-bottom: 2rem;" />

	<form action="./randomEncounter" class="encounterButtonTop">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton" value="Random Encounter" />
	</form>

	<form action="./randomEasyEncounter" class="encounterButtonTop">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton" value="Random Easy Encounter" />
	</form>

	<form action="./randomMediumEncounter" class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton" value="Random Medium Encounter" />
	</form>

	<form action="./randomHardEncounter" class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton" value="Random Hard Encounter" />
	</form>

	<form action="./randomDeadlyEncounter" class="encounterButtonBottom">
		<input type="hidden" name="level" class="level" value="1" /> <input
			type="hidden" name="number" class="number" value="1" /> <input
			type="submit" class="encounterButton" value="Random Deadly Encounter" />
	</form>

	<script>
		function changeLevel(e) {
			var list = document.getElementsByClassName("level");
			var i;
			for (i = 0; i < list.length; i++) {
				list[i].value = e.target.value;
			}
		}
		function changeNumber(e) {
			var list = document.getElementsByClassName("number");
			var i;
			for (i = 0; i < list.length; i++) {
				list[i].value = e.target.value;
			}
		}

		window.onload = function() {
			var list = document.getElementsByClassName("level");
			var i;
			var e = document.getElementById("levelSelect");
			for (i = 0; i < list.length; i++) {
				list[i].value = e.options[e.selectedIndex].value;
			}

			list = document.getElementsByClassName("number");
			e = document.getElementById("numberSelect");
			for (i = 0; i < list.length; i++) {
				list[i].value = e.options[e.selectedIndex].value;
			}
		}
	</script>

</body>
</html>
