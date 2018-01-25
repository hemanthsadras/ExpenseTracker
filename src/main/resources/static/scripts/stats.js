 $(document).ready(function() {
	
     var token = localStorage.getItem("token");
     var baseUrl = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
     
     if(token == null) {
 		window.location.href = baseUrl + "/login.html";
 	 }
     
     initialize();
     var today = new Date();
     var currentMonth = 1 + today.getMonth();
     var currentYear = today.getFullYear();
     drawCategoryProjectionChart(currentMonth,currentYear);
     drawMonthlyExpenseChart(currentMonth,currentYear);
     drawYearlyProjectionChart(currentYear);

     
    
    
    function drawMonthlyExpenseChart(month, year) {
    	
    	 $.ajax({
        	 url : baseUrl + "/analysis/month/" + month + "/" + year,
        	 headers : {
        		 "Authorization" : token
        	 },
        	 success : function(results) {
        		 
        		 var dataPointsForChart = [];
        		 
        		 for(var index = 0; index < results.length; index++) {
        			 var dataObject = new Object();
        			 dataObject.label = results[index].day;
        			 dataObject.value = results[index].amount;
        			 dataPointsForChart.push(dataObject);
        		 }
        		 
        		// enter chart information here
        		 FusionCharts.ready(function(){
         		      var monthlyChart = new FusionCharts({
         		        type: "line",
         		        renderAt: "chartContainerForLine",
         		        width: "85%",
         		        height: "300",
         		        dataFormat: "json",
         		        dataSource: {
         		          chart: {
         		              caption: "Monthly spending",
         		              xAxisName: "Month",
         		              yAxisName: "Spending (In INR)",
         		              theme: "fint"
         		           },
         		          data: 	dataPointsForChart        		        
         		        }
         		    });

         		     monthlyChart.render();
         		})
        		// end here
        	 }
         });
    }
    
    
    function drawYearlyProjectionChart(year) {
    	
    	$.ajax({
        	url : baseUrl + "/analysis/year/" + year,
        	headers : {
        		"Authorization" : token
        	},
        	
        	success : function(results) {
        		var dataPointsForChart = [];
        		
        		for(var index = 0; index < results.length; index++) {
        			var monthlySummary = new Object();
        			monthlySummary.value = results[index].totalAmount;
        			monthlySummary.label = results[index].monthName;
        			
        			dataPointsForChart.push(monthlySummary);
        		}
        		 
        		
        		// enter chart information here
        		FusionCharts.ready(function(){
      		      var yearlyChart = new FusionCharts({
      		        type: "column2d",
      		        renderAt: "yearlychartContainer",
      		        width: "85%",
      		        height: "300",
      		        dataFormat: "json",
      		        dataSource: {
      		          chart: {
      		              caption: "Monthly spending",
      		              xAxisName: "Month",
      		              yAxisName: "Spending (In INR)",
      		              theme: "fint"
      		           },
      		          data: 	dataPointsForChart        		        
      		        }
      		    });

      		    yearlyChart.render();
      		})
        		// ends here - chart
        		
        	}
        });
    }
    
    
	function drawCategoryProjectionChart(month, year) {
	    	
	    	$.ajax({
	        	url : baseUrl + "/analysis/category/" + month + "/" + year, 
	        	headers : {
	        		"Authorization" : token
	        	},
	        	
	        	success : function(results) {
	        		var dataPointsForChart = [];
	        		
	        		for(var index = 0; index < results.length; index++) {
	        			var categorySummary = new Object();
	        			categorySummary.value = results[index].totalAmount;
	        			categorySummary.label = results[index].categoryName;
	        			
	        			dataPointsForChart.push(categorySummary);
	        		}
	        		
	        		// enter chart information here
	        		FusionCharts.ready(function(){
	        		      var categoryChart = new FusionCharts({
	        		        type: "column2d",
	        		        renderAt: "categoryChartContainer",
	        		        width: "85%",
	        		        height: "300",
	        		        dataFormat: "json",
	        		        dataSource: {
	        		          chart: {
	        		              caption: "Category wise spending",
	        		              xAxisName: "Category",
	        		              yAxisName: "Spending (In INR)",
	        		              theme: "fint"
	        		           },
	        		          data: 	dataPointsForChart        		        
	        		       }
	        		    });

	        		      categoryChart.render();
	        		})
	        		// ends chart info here
	        	}
	        });
	    }
	
	
	
	function initialize() {
		 var date_input_category =$("#datePickerForCategory");
		 var date_input_monthly = $("#datePickerForMonth");
		 var date_input_yearly = $("#datePickerForYear");
		 
		 var optionsForMonthCalender = {
				 format : "mm/yyyy",
				 startView: "months", 
		    	 minViewMode: "months",
		    	 todayHighlight: true,
		         autoclose: true,			 
		 };
		 
		 var optionsForYearCalender = {
				 format : "yyyy",
				 startView: "years", 
		    	 minViewMode: "years",
		    	 todayHighlight: true,
		         autoclose: true,
		 };
		 
		 var dateForCategory = date_input_category.datepicker(optionsForMonthCalender);
		 var dateForMonthly = date_input_monthly.datepicker(optionsForMonthCalender);
		 var dateForYearly = date_input_yearly.datepicker(optionsForYearCalender);
		 date_input_category.datepicker('update', new Date());
		 date_input_monthly.datepicker('update', new Date());
		 date_input_yearly.datepicker('update', new Date());
		 
		 dateForCategory.on("changeDate", function(date){
			 var datePicker = date.target.value;
			 var monthYear = datePicker.split("/");
			 drawCategoryProjectionChart(monthYear[0], monthYear[1]);
			 
		 });
		 
		 dateForMonthly.on("changeDate", function(date){
			 var datePicker = date.target.value;
			 var monthYear = datePicker.split("/");
			 drawMonthlyExpenseChart(monthYear[0], monthYear[1]);
			 
		 });
		 
		 dateForYearly.on("changeDate", function(date){
			 var year = date.target.value;	
			 drawYearlyProjectionChart(year);
			 
		 });
	}
	 
    
    $("#logoutBtn").click(function(){
			localStorage.clear();
			window.location.href = baseUrl + "/login.html";
		});
     
     
 });