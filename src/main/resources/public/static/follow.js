console.log("in follow javacscript file");

// AJAX posting x-www-form-urlencoded

  
var button = document.getElementById('follow_button');
document.getElementById("myInput").focus();

function mySort() {
  var input, filter, table, ul, li, i;
  input = document.getElementById("myInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("users");
  ul = table.getElementsByTagName("ul");
  for (i = 0; i < ul.length; i++) {
    li = ul[i].getElementsByTagName("li")[0];
    if (li) {
      if (li.innerHTML.toUpperCase().indexOf(filter) > -1) {
        ul[i].style.display = "";
      } else {
        ul[i].style.display = "none";
      }
    }       
  }
}



    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/follow_submit');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
    	if (xhr.status === 200) {
    		console.log("in follow js in the rc 200 section");
			window.location.assign("/follow");
        } else if (xhr.status !== 200) {
            alert('Request failed.  Returned status of ' + xhr.status);
        }
    };
    
    function myFunction(id) {
    var body = 'target_id=' + encodeURIComponent(id);
    console.log(body);
    xhr.send(body);}