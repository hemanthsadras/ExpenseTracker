 $(document).ready(function() {
	  
    	var token = localStorage.getItem("token");
    	var baseUrl = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
    	
    	if(token == null) {
    		window.location.href = baseUrl + "/login.html";
    	}
    	
    	initializeTable();
    	initializeDateFields();
    	
    	 
    	  			

    	  	    	function CategoryViewModel(categories) {
    	  	    		var self = this;
    	  	    		self.userCategories = ko.observableArray(categories);
    	  	    		self.addCategory = function() {
    	  	    			var categoryRequest = new Object();
    	  	    			categoryRequest.categoryName = $("#newcategory").val();
    	  	    			
    	  	    			$.ajax({
    	  	    				url : baseUrl + "/category",
    	  	    				headers : {
    	    	  					'Authorization' : token
    	    	  				},
    	  	    				type : "POST",
    	  	    				data : JSON.stringify(categoryRequest),
    	  	    				contentType : "application/json",
    	  	    				newCategory : categoryRequest.categoryName,
    	  	    				viewModel : self,
    	  	    				success : function(result) {
    	  	    					this.viewModel.userCategories.push(this.newCategory);
    	  	    					$("#category").val(this.newCategory);
    	  	    				}
    	  	    			});
    	  	    		}
    	  	    	}
    	  	    	
    	  	    	var self = this;
    	  	    	var categoryViewModel;
    	  			
    	  			$.ajax({
    	  				url : baseUrl + "/category",
    	  				headers : {
    	  					'Authorization' : token
    	  				},
    	  				mainReference : this,
    	  				success : function(result) {
    	  					this.mainReference.categoryViewModel = new CategoryViewModel(result);
    	  					ko.applyBindings(this.mainReference.categoryViewModel);
    	  				}
    	  				
    	  			});
    	  			
    	  			$("#expenses").on('click',  'span.deleteBtn',function(data) {
    	  				
    	  				var expenseTable = $("#expenses").DataTable();
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
                   
    	  			$("#addExpenseBtn").click(function() {
    	  				var amountEntered = $("#amount").val();
	  					var categorySelected = $("#category").val();
	  					var descriptionEntered = $("#description").val();
	  					var dateEntered = $("#datepicker").val();
	  					
	  					var expenseModel = new Object();
	  					expenseModel.amount = amountEntered;
	  					expenseModel.category = categorySelected;
	  					expenseModel.description = descriptionEntered;
	  					expenseModel.date = new Object();
	  					expenseModel.date.day = dateEntered.split("/")[0];
	  					expenseModel.date.month = dateEntered.split("/")[1];
	  					expenseModel.date.year = dateEntered.split("/")[2];
	  					
	  					$.ajax({
	  						type : "POST",
	  						url : baseUrl + "/expense",
	  						headers : {
	    	  					'Authorization' : token
	    	  				},
	  						data : JSON.stringify(expenseModel),
	  						contentType : "application/json",
	  						success : function(result) {
	  							var expenseTable = $("#expenses").DataTable();
	  							
	  							var today = new Date();
	  							var currentMonth = today.getMonth();
	  							var currentYear = today.getFullYear();
	  							
	  							if(result.date.month == (currentMonth+1) && result.date.year == currentYear) {
	  								expenseTable.row.add(result).draw();
	  							}
	  							
	  							$("#amount").val("");
	  		  					$("#category").val("Food");
	  		  					$("#description").val("");
	  		  					$("#datepicker").val("");
	  						}
	  						
	  					});
    	  			});
    	  			
    	  			$("#logoutBtn").click(function(){
    	  				localStorage.clear();
    	  				window.location.href = baseUrl + "/login.html";
    	  			});
    	  			
    	  			
    	  			function initializeDateFields() {
    	  				var date_input_monthly = $("#datepicker");
    	  				var optionsForCalender = {
    	  						 format : "dd/mm/yyyy",
    	  				    	 todayHighlight: true,
    	  				         autoclose: true,			 
    	  				 };
    	  				
    	  				date_input_monthly.datepicker(optionsForCalender);
    	  				date_input_monthly.datepicker('update', new Date());
    	  				 
    	  			}
    	  			
    	  			
    	  			function initializeTable() {
    	  				
    	  				$.ajax({
        	  				url : baseUrl + "/expense/month",
        	  				headers : {
        	  					'Authorization' : token
        	  				},
        	  				success : function(result) {
        	  					$('#expenses').DataTable({
        	  						"aaData" : result,
        	  						"language": {
        	  					      "emptyTable": "No expenses are recorded for current month"
        	  					    },
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
    	  			
    	  		
            } );
