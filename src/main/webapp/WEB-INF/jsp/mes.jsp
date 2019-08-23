<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<script src="js/mes.js"></script>
<head>
<link rel="stylesheet" href="css/site.css" />

<meta charset="UTF-8">
<title>Minor's World</title>
</head>
<body>
<div id='mes-main-content'>
	<input type="hidden" id="resultPointSet" value='${resultPointSet}' />
	<input type="hidden" id="inputPointSet" value='${inputPointSet}' />
	
	<a href="${homeURL }"><span id="homeButton">Home</span></a>
	<h2 id="title">Minimal Enclosing Shape</h2>
	<h4 id="subtitle">Add points to the grid by clicking on them or
		generate random points. Press Calculate to find the Minimal Enclosing Shape.</h4>
	
	<div id="input-area">
		<table>
			<tbody id="input-table">
				<c:forEach begin='0' end='${sizeY-1 }' var='i'>
					<tr>
						<c:forEach begin='0' end='${sizeX-1 }' var='j'>
							<td class='mes-point' data-x='${j}' data-y='${i}'
								id='point-${j}-${i}'></td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div id="forms-output">
			<div >
				<c:url value="/calculate" var="formAction" />
				<form id="calculate-form" action='${formAction }' method='POST'>
					<input type="hidden" name="pointsInput" id="pointsInputHandler">
					<input class="btn btn-bold" type="submit" value="Calculate">
				</form>
				<form id="clear-form">
					<input class="btn" type="submit" value="Clear">
				</form>
			</div>
			<div>
				<h4>Generate test input</h4>
				<c:url value="/randomize" var="formAction" />
				<form id="randomize-form" action='${formAction }' method='GET'>
					<div class="form-item">
						<label for="d-field">Density</label> <input type="number" name="d"
						id="d-field" step="0.05" value="${density }" min="0.00" max="1.00">
					</div>
					<div class="form-item">
						<label for="f-field">Flocculation</label> <input type="number"
							name="f" id="f-field" step="0.05" value="${flocculation }"
							min="0.00" max="1.00"> 
					</div>
					<div class="form-item">	
						<input class="btn" type="submit" value="Generate">
					</div>
				</form>
			</div>
			<div id="output-area">
				<c:forEach var="infoItem" items="${runInfo }">
					<p>${infoItem }</p>
				</c:forEach>
			</div>
		</div>
		
	</div>
<div id="additional-info">
		This application is powered by Java and Spring MVC.
		Find the source code and a command-line interface on <a href=https://github.com/mc-in-cle>my Github.</a>
		</div>
</div>

</body>
<footer>
<p>&copy; Minor Cline 2018</p>
</footer>
</html>