<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="<c:url value="/static/css/style.css"/>" />
<link href="https://afeld.github.io/emoji-css/emoji.css"
	rel="stylesheet">
<title>Npc Details</title>
</head>
<body>
	<%@include file="./header.jsp"%>

	<h1>Npc Details</h1>

	<div id="npcDetailsContainer">
		<div class="npcCommonDetails">
			<h2 id="npcLevel">Level: ${npc.level}</h2>
			<h2 id="npcClass">${npc.classname}
				<c:if test="${npc.subclass != null}">
					(${npc.subclass})
				</c:if>
			</h2>
		</div>
		<div class="npcCommonDetails">
			<h3 class="npcSubCommonDetails">${npc.race}</h3>
			<h3 class="npcSubCommonDetails">${npc.gender}</h3>
			<h3 class="npcSubCommonDetails">${npc.age} yrs</h3>
		</div>
		<div id="npcStatBlock">
			<h3>Str : ${npc.strength}</h3>
			<h3>Dex : ${npc.dexterity}</h3>
			<h3>Con : ${npc.constitution}</h3>
			<h3>INT : ${npc.intelligence}</h3>
			<h3>WIS : ${npc.wisdom}</h3>
			<h3>CHA : ${npc.charisma}</h3>
		</div>
		<div class="npcVariableDetails">
			<h3 id="npcAC">AC: ${npc.ac}</h3>
			<h3 id="npcSpeed">Speed: ${npc.speed}</h3>
			<h3 id="npcHP">HP: ${npc.hp}</h3>
			<h3 id="npcHitDice">Hit Dice: ${npc.level}d${npc.hitDice}</h3>
		</div>
		<div id="npcFeatsDiv">
			<h3 id="npcFeatsHeading">Feats</h3>
			<p id="npcFeatsList">
				<c:forEach items="${npc.feats}" var="f" varStatus="status">
					${f}
					<c:if test="${!status.isLast()}">
					, 
					</c:if>
				</c:forEach>
			</p>
		</div>
		<div id="npcInventoryDiv">
			<h3 id="npcFeatsHeading">Inventory</h3>
			<p id="npcInventory">
				<c:forEach items="${npc.inventory}" var="i" varStatus="status">
				${i.name}
				<c:if test="${i.amount > 1}">
					(${i.amount})
				</c:if>
					<c:if test="${!status.isLast()}">
					, 
				</c:if>
				</c:forEach>
			</p>
		</div>
	</div>
</body>
</html>