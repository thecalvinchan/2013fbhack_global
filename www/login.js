window.fbAsyncInit = function() {
  FB.init({
    appId : '244552545701869',
    oauth : true,
    status : true,
    cookie : true,
    xfbml : true,
  });
};

function fb_login() {
  FB.login(function(response) {
    if (response.authResponse) {
      console.log(response);
      access_token = response.authResponse.accessToken;
      user_id = response.authResponse.userID;

      FB.api('/me', function(response) {
        user_email = response.email;
      });
      window.location = "./timeline/index.html";
    } else {
      console.log('cancelled');
    }
  }, {
    scope: 'publish_stream, email'
  });
}

(function() {
  var e = document.createElement('script');
  e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
  e.async = true;
  document.getElementById('fb-root').appendChild(e);
}());
