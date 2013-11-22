window.onload=function() {
  var data = null;
  data = new XMLHttpRequest();
  data.open("GET", "http://54.201.41.99/photos/?user=1234", false);
  data.send(null);
  obj = JSON.parse(data.responseText);
  var stackrow = document.createElement('div')
  stackrow.setAttribute('class', 'stack-row row');
  console.log(obj);
  for (var stack in obj) {
    console.log(obj[stack]);
    var stackstack = document.createElement('div');
    stackstack.setAttribute('class', 'stack-stack');
    var crop = document.createElement('div');
    crop.setAttribute('class', 'crop');
    var img = document.createElement('img');
    img.setAttribute('src', "http://54.201.41.99"+obj[stack].pictures[0]);
    crop.appendChild(img);
    
    // I'D ADD A FUCKING NAME BUT I GUESS IT DOESN'T WANT TO BE FUCKING ADDED
    // EVEN THOUGH IT'S LITERALLY FUCKING IDENTICAL TO EVERY OTHER CREATEELEMENT
    // JAVASCRIPT A SHIT
    stackstack.appendChild(crop);
    stackrow.appendChild(stackstack);
  }
  document.getElementById('stacks').appendChild(stackrow);
};
