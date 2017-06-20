//KR 6/19 moved to TWITTERCLONE.HTML

// AJAX posting x-www-form-urlencoded
var button = document.getElementsByTagName('button');

button.onclick = function () {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/like');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
    	if (xhr.status === 200) {
			console.log('Request passed');
        } else if (xhr.status !== 200) {
            alert('Request failed.  Returned status of ' + xhr.status);
        }
    };
    
    function myLikes(tweet_id) {
    var body = 'tweet_id=' + encodeURIComponent(tweet_id);
    console.log(body);
    xhr.send(body); }