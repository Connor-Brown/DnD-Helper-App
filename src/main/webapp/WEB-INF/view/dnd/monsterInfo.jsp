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
<title>${monster.name}Info</title>
</head>
<body>
	<%@include file="./header.jsp"%>
	<br />
	<br />
	<br />

	<c:choose>
		<c:when test="${monster.isLegendary()}">
			<div class="monsterInfoDivLegendary">
		</c:when>
		<c:otherwise>
			<div class="monsterInfoDiv">
		</c:otherwise>
	</c:choose>
	<h1>
		<b><a
			href="https://www.google.com/search?tbm=isch&q=D%26D+${monster.name}"
			target="blank">${monster.name}</a></b>
	</h1>
	<h4>
		<c:choose>
			<c:when test="${monster.size == 'T'}">
				<i>Tiny ${monster.type}, ${monster.alignment1}
					${monster.alignment2}</i>
			</c:when>
			<c:when test="${monster.size == 'S'}">
				<i>Tiny ${monster.type}, ${monster.alignment1}
					${monster.alignment2}</i>
			</c:when>
			<c:when test="${monster.size == 'M'}">
				<i>Medium ${monster.type}, ${monster.alignment1}
					${monster.alignment2}</i>
			</c:when>
			<c:when test="${monster.size == 'L'}">
				<i>Large ${monster.type}, ${monster.alignment1}
					${monster.alignment2}</i>
			</c:when>
			<c:when test="${monster.size == 'H'}">
				<i>Huge ${monster.type}, ${monster.alignment1}
					${monster.alignment2}</i>
			</c:when>
			<c:otherwise>
				<i>Gargantuan ${monster.type}, ${monster.alignment1}
					${monster.alignment2}</i>
			</c:otherwise>
		</c:choose>
	</h4>
	<hr />
	<h3>Armor Class ${monster.ac}</h3>
	<h3>Hit Points ${monster.averageHp}
		(${monster.numDice}d${monster.diceSize}+${monster.bonusHp})</h3>

	<h3>Challenge Rating ${monster.cr}</h3>

	<hr />

	<c:forEach var="innerText" items="${monster.innerTextList}"
		varStatus="status">
		<br />
		<div class="monsterTraitDiv">
			<p>
				<b>${innerText.name}</b>
			</p>
			<p>${innerText.text}</p>
			<br />
		</div>
	</c:forEach>

	</div>

	<br />
	<br />
	<br />

</body>
</html>