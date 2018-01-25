 $(document).ready(function() {
	
     var token = localStorage.getItem("token");   
     var baseUrl = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
     if(token == null) {
 		window.location.href = baseUrl + "/login.html"
 	}
     
     initialize();
     var today = new Date();
     drawExpenseTable(1 + today.getMonth(),today.getFullYear());
   
     $("#calenderBtn").click(function() {
    	 var dateValue = $("#datePicker").val();
    	 var year = dateValue.split("/")[1];
    	 var month = dateValue.split("/")[0];
         drawExpenseTable(month, year);	
     });
     
     $("#expenseTable").on('click',  'span.deleteBtn',function(data) {
			
			var expenseTable = $("#expenseTable").DataTable();
			var expenseIdToBeDeleted = expenseTable.row($(this).parents('tr')).data().expenseId;
			var resturi = baseUrl + "/expense/" + expenseIdToBeDeleted;
			$.ajax({
				type : "DELETE",
				url : resturi,
				headers : {
					'Authorization' : token
				},
				row : this,
				success : function(result) {
					expenseTable.row($(this.row).parents('tr'))
				     					.remove()
				     					.draw();
				}
			});
			
		});
     
     $("#logoutBtn").click(function(){
			localStorage.clear();
			window.location.href = baseUrl + "/login.html";
		});
     
     
     function initialize() {
    	 var date_input_monthly = $("#datePickerForMonth");
    	 var optionsForMonthCalender = {
				 format : "mm/yyyy",
				 startView: "months", 
		    	 minViewMode: "months",
		    	 todayHighlight: true,
		         autoclose: true,			 
		 };
    	 
    	 var dateForMonthly = date_input_monthly.datepicker(optionsForMonthCalender);
    	 date_input_monthly.datepicker('update', new Date());
    	 
    	 dateForMonthly.on("changeDate", function(date){
			 var datePicker = date.target.value;
			 var monthYear = datePicker.split("/");
			 drawExpenseTable(monthYear[0], monthYear[1]);
			 
		 });
     }
     
     function drawExpenseTable(month,year) {
    	 var resturi = baseUrl + "/expense/" + year + "/" + month;
			$.ajax({
				url : resturi,
				headers : {
					'Authorization' : token
				},
				success : function(result) {
					$('#expenseTable').DataTable({
						"destroy" : true,
						"language": {
	  					      "emptyTable": "No expenses are recorded for selected period"
	  					    },
						"aaData" : result,
					    "bAutoWidth" : false,
					     "columns" : [
					    	 {
					    		 data : "date.expenseDate"
					    	 },
					    	 {
					    		 data : "amount"
					    	 },
					    	 {
					    		 data : "category"
					    	 }, 
					    	 {
					    		 data : "description"
					    	 },
					    	 {
					    		 data : null,
					    		 render : function() {
					    			 return '<span class="glyphicon glyphicon-trash inlineBtn deleteBtn"></span>';
					    		 }
					    	 }
					    
					    	 
					     ]
					});
				}
			});
	
     }
     
     
 });