$(document).ready(function(){
	var baseUrl = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
	  $("#register").click(function(){
		  var emailId = $("#emailId").val();
		  var username = $("#username").val();
		  var password = $("#password").val();
		  var confirmPassword = $("#confirmPassword").val();
		  
		  if(password != confirmPassword || (emailId == "") || (username == "")) {
			  var errorModal = $("#errorModal");
			  errorModal.modal('show');
		  }
		  else {
			  
			  var accountDetails = new Object();
			  accountDetails.userName = username;
			  accountDetails.password = password;
			  accountDetails.emailId = emailId;
			  
			  
			  $.ajax({
				  type : "POST",
				  url : baseUrl + "/account",
				  data : JSON.stringify(accountDetails),
				  contentType : "application/json",
				  success : function(data) {
					  var successModal = $("#successModal");
					  successModal.modal('show');
				  }
			  });
			  
		  }
	  });
	  
	  $('#successModal').on('hidden.bs.modal', function () {
		  window.location.href = baseUrl + "/login.html";
		});
});