console.log("in follow javacscript file");

// AJAX posting x-www-form-urlencoded
var button = document.getElementById('follow_button');
//var searchid = document.getElementById('search_id');
//console.log("search id "+searchid);
var targetid = document.getElementById('target_id');
console.log("target id "+targetid);

/* var p = document.createElement('p');
var div = document.getElementById('msg'); */

button.onclick = function () {
//	var search_id = searchid.value;
	var target_id = targetid.value;
	console.log("search value = "+search_id);
	console.log("target value = "+target_id);
		
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/follow_submit');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
    	if (xhr.status === 200) {
 /*       	div.appendChild(p);
    		p.innerHTML = "Your tweet has been posted!";
    		setTimeout(function() {
    			p.innerHTML = "";}, 2000); */
        } else if (xhr.status !== 200) {
            alert('Request failed.  Returned status of ' + xhr.status);
        }
    };
//    var body = 'target_id=' + encodeURIComponent(target_id) + '&search_id=' + encodeURIComponent(search_id);
    var body = 'target_id=' + encodeURIComponent(target_id);
    console.log(body);
    xhr.send(body);
};
