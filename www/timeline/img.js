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
    var url = obj[stack].pictures[1];
    img.setAttribute('src', "http://54.201.41.99"+url.substring(8, url.length ));
    crop.appendChild(img);
    var fn = document.createElement('h5');
    
    // Turns out appendChild needs a DOM element and doesn't work with plain text
    // Remind me again why people use vanilla Javascript?????
    var nametext = document.createTextNode(obj[stack].name);
    fn.appendChild(nametext);
    stackstack.appendChild(crop);
    stackstack.appendChild(fn);
    stackstack.setAttribute('transcript', obj[stack].transcript);
    stackstack.onclick = function() {
      document.getElementById('transcript-text').innerHTML=this.getAttribute('transcript');
      document.getElementById('overlay').style.display='block';
      document.getElementById('fade').style.display='block';
    }

    stackrow.appendChild(stackstack);
  }
  document.getElementById('stacks').appendChild(stackrow);
};
