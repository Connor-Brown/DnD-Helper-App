<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/static/css/dnd-style.css"/>" />
<link href="https://afeld.github.io/emoji-css/emoji.css"
	rel="stylesheet">
<title>Npc Generator Home</title>
</head>
<body>
	<%@include file="./header.jsp"%>

	<h1>Npc Generator Home Page</h1>

	<div class="npcHomeSelects">
		<p>Select the characters class</p>
		<select id="classSelectNpc" onchange="changeClass(event)">
			<option value="Surprise me" selected="selected">Surprise me</option>
			<option value="BARBARIAN">BARBARIAN</option>
			<option value="BARD">BARD</option>
			<option value="CLERIC">CLERIC</option>
			<option value="DRUID">DRUID</option>
			<option value="FIGHTER">FIGHTER</option>
			<option value="MONK">MONK</option>
			<option value="PALADIN">PALADIN</option>
			<option value="RANGER">RANGER</option>
			<option value="ROGUE">ROGUE</option>
			<option value="SORCERER">SORCERER</option>
			<option value="WARLOCK">WARLOCK</option>
			<option value="WIZARD">WIZARD</option>
		</select>
	</div>

	<div class="npcHomeSelects">
		<p>Select the characters race</p>
		<select id="raceSelect" onchange="changeRace(event)">
			<option value="Surprise me" selected="selected">Surprise me</option>
			<option value="AARAKOCRA">AARAKOCRA</option>
			<option value="AASIMAR">AASIMAR</option>
			<option value="CHANGELING">CHANGELING</option>
			<option value="DRAGONBORN">DRAGONBORN</option>
			<option value="DWARF">DWARF</option>
			<option value="ELF">ELF</option>
			<option value="FIRBOLG">FIRBOLG</option>
			<option value="GENASI">GENASI</option>
			<option value="GNOME">GNOME</option>
			<option value="GOLIATH">GOLIATH</option>
			<option value="HALF-ELF">HALF-ELF</option>
			<option value="HALF-ORC">HALF-ORC</option>
			<option value="HUMAN">HUMAN</option>
			<option value="LIZARDFOLK">LIZARDFOLK</option>
			<option value="LOXODON">LOXODON</option>
			<option value="ORC">ORC</option>
			<option value="SHIFTER">SHIFTER</option>
			<option value="TABAXI">TABAXI</option>
			<option value="TIEFLING">TIEFLING</option>
			<option value="TORTLE">TORTLE</option>
			<option value="TRITON">TRITON</option>
			<option value="WARFORGED">WARFORGED</option>
		</select>
	</div>

	<div class="npcHomeSelects">
		<p>Select the characters level</p>
		<select id="levelSelect" onchange="changeLevel(event)">
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

	<form action="./generateNpc" class="npcForm">
		<input type="hidden" name="level" class="level" value="Surprise me" />
		<input type="hidden" name="race" class="race" value="Surprise me" />
		<input type="hidden" name="classname" class="classname" value="Surprise me" /> 
		<input type="submit" class="npcButton" value="Generate Npc" />
	</form>

	<script>
		function changeLevel(e) {
			var list = document.getElementsByClassName("level");
			var i;
			for (i = 0; i < list.length; i++) {
				list[i].value = e.target.value;
			}
		}
		function changeClass(e) {
			var list = document.getElementsByClassName("classname");
			var i;
			for (i = 0; i < list.length; i++) {
				list[i].value = e.target.value;
			}
		}
		function changeRace(e) {
			var list = document.getElementsByClassName("race");
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

			list = document.getElementsByClassName("race");
			e = document.getElementById("raceSelect");
			for (i = 0; i < list.length; i++) {
				list[i].value = e.options[e.selectedIndex].value;
			}
			
			list = document.getElementsByClassName("classname");
			e = document.getElementById("classSelectNpc");
			for (i = 0; i < list.length; i++) {
				list[i].value = e.options[e.selectedIndex].value;
			}
		}
	</script>

</body>
</html>