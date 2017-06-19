console.log("in follow javacscript file");

// AJAX posting x-www-form-urlencoded


/* var p = document.createElement('p');
var div = document.getElementById('msg'); */

var buttons = document.getElementsByTagName('button');
alert(buttons.length);
 button.onclick = function () {

var userid = document.getElementById('user_id');
alert("user id = "+ userid);
alert("get element by id ="+follow_button);
follow_button.addEventListener('click',function(evt) {
    if (evt.target.getAttribute('userid' == "[object HTMLButtonElement]")) {
    alert(evt.target.getAttribute("userid") + " click") ;} })
    
var button = document.getElementById('follow_button');
//var searchid = document.getElementById('search_id');
//console.log("search id "+searchid);
var targetid = document.getElementById('target_id');
console.log("target id "+targetid);


//	var search_id = searchid.value;
	var target_id = targetid.value;
	alert(this.id);
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
    		console.log("in follow js in the rc 200 section");
			window.location.assign("/follow");
        } else if (xhr.status !== 200) {
            alert('Request failed.  Returned status of ' + xhr.status);
        }
    };
//    var body = 'target_id=' + encodeURIComponent(target_id) + '&search_id=' + encodeURIComponent(search_id);
    var body = 'target_id=' + encodeURIComponent(target_id);
    console.log(body);
    xhr.send(body);
};  
