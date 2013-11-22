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
        window.document.title = response.name + "\'s Timeline";
      });
      FB.api('/me/picture?type=large', function(response) {
        console.log(response);
      });
    } else {
      console.log('failed');
    }
  });
};

(function() {
  var e = document.createElement('script');
  e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
  e.async = true;
  document.getElementById('fb-root').appendChild(e);
}());
