
document.addEventListener('DOMContentLoaded', () => {

	document.querySelectorAll('.mes-point').forEach( (point) => {
		point.addEventListener('click', (event) =>{
			togglePointInput(event.currentTarget);
			event.stopPropagation();
		});
	});
	
	showPointValues();
	
	const randomizeForm = document.getElementById('randomize-form');
	randomizeForm.addEventListener('submit', clearGrid);
	
	const calculateForm = document.getElementById('calculate-form');
	calculateForm.addEventListener('submit', (event) =>{
		collectPoints();
	});
	
	const clearButton = document.getElementById('clear-form').querySelector('input');
	clearButton.addEventListener('click', (event) => {
		event.preventDefault();
		clearGrid();
	})
	
});

function showPointValues(){
	const resultJSON = document.getElementById('resultPointSet').value;
	const resultList = getPointElementsFromJSON(resultJSON);
	resultList.forEach( (point) => {
		point.classList.add('point-result');
	});
	const inputJSON = document.getElementById('inputPointSet').value;
	const inputList = getPointElementsFromJSON(inputJSON);
	inputList.forEach( (point) => {
		point.classList.add('point-input');
	});
}

function getPointElementsFromJSON(json){
	let points = [];
	if (json != ""){
		const list = JSON.parse(json);
		list.forEach( (point) => {
			points.push(document.getElementById("point-" + point.x + "-" + point.y));
		});
	}
	return points;
}

function togglePointInput(point){
	if (point.classList.contains('point-input'))
		point.classList.remove('point-input');
	else
		point.classList.add('point-input');
	if (point.classList.contains('point-result'))
		point.classList.remove('point-result');
}


function collectPoints(){
	const pointsInputHandler = document.getElementById('pointsInputHandler');
	const allPoints = document.getElementById('input-table').querySelectorAll('.mes-point');
	let pointlist = [];
	
	allPoints.forEach((point) => {
		if (point.classList.contains('point-input')){
			pointlist.push({x: point.dataset.x, y: point.dataset.y});
		}
	});
	pointsInputHandler.value = JSON.stringify(pointlist);
}

function clearGrid(){
	document.getElementById('resultPointSet').value = '';
	document.getElementById('inputPointSet').value = '';
	document.getElementById('output-area').innerText = '';
	document.querySelectorAll('.mes-point').forEach( (point) => {
		point.classList.remove('point-result');
		point.classList.remove('point-input');
	});
}

