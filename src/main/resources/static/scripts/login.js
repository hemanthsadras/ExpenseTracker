$(window, document, undefined).ready(function() {
	var baseUrl = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
  $('input').blur(function() {
    var $this = $(this);
    if ($this.val())
      $this.addClass('used');
    else
      $this.removeClass('used');
  });

  var $ripples = $('.ripples');

  $ripples.on('click.Ripples', function(e) {

    var $this = $(this);
    var $offset = $this.parent().offset();
    var $circle = $this.find('.ripplesCircle');

    var x = e.pageX - $offset.left;
    var y = e.pageY - $offset.top;

    $circle.css({
      top: y + 'px',
      left: x + 'px'
    });

    $this.addClass('is-active');

  });

  $ripples.on('animationend webkitAnimationEnd mozAnimationEnd oanimationend MSAnimationEnd', function(e) {
  	$(this).removeClass('is-active');
  });
  
  
  $("#loginBtn").click(function(){
	  var username = $("#username").val();
	  var password = $("#password").val();
	  var authenticationRequest = new Object();
	  authenticationRequest.username = username;
	  authenticationRequest.password = password;
	  
	  $.ajax({
			type : "POST",
			url : baseUrl + "/auth",
			data : JSON.stringify(authenticationRequest),
			contentType : "application/json",
			success : function(result) {
				localStorage.setItem("token", "Bearer " + result.token);
				document.location.href = baseUrl + "/index.html";
				
			},
	  		error : function(result) {
	  			alert("invalid credentials");
	  		}
			
		});
  });
  
  $("#registerBtn").click(function(){
	  window.location.href = baseUrl + "/signup.html";
  });

});