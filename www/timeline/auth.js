window.fbAsyncInit = function() {
  FB.init({
    appId : '244552545701869',
    oauth : true,
    status : true,
    cookie : true,
    xfbml : true,
  });

  FB.getLoginStatus(function(response) {
    if (response.status === 'connected') {
      FB.api('/me', function(response) {
        var txt = response.name + '\'s New Acquaintances';
        window.document.title = txt;
        document.getElementById('header-title').innerHTML=txt;
      });
      FB.api('/me/picture?type=large', function(response) {
        var i = document.createElement('img');
        i.src = response.data.url;
        document.getElementById('header-image').appendChild(i);  
      });
    } else {
      window.location = "../index.html";
    }
  });
};

(function() {
  var e = document.createElement('script');
  e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
  e.async = true;
  document.getElementById('fb-root').appendChild(e);
}());
