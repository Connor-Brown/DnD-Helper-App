<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/static/css/dnd-style.css"/>" />
<link href="https://afeld.github.io/emoji-css/emoji.css" rel="stylesheet">
<title>Home</title>
</head>
<body>
	<%@include file="./header.jsp" %>
	
	<h1>Welcome to my Dungeons and Dragons Helper App!</h1>
	
	<form action="/dnd/encounterGenerator/home" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Encounter Generator"/>
	</form>
	
	<form action="/dnd/encounterMaker/home" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Encounter Maker"/>
	</form>
	
	<form action="/dnd/treasureGenerator/home" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Treasure Generator"/>
	</form>
	
	<form action="/dnd/itemList/home" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Item List"/>
	</form>
	
	<form action="/dnd/npcGenerator/home" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Npc Generator"/>
	</form>
	
	<form action="/dnd/spellService/home" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Spell List"/>
	</form>
	
	<form action="/dnd/create/item" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Create Item"/>
	</form>
	
	<form action="/dnd/create/monster" class="frontPageForm">
		<input class="frontPageButton" type="submit" value="Create Monster"/>
	</form>
	
</body>
</html>