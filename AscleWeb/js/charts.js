var toChartData = [];

function drawChart() 
{
/*
	c_data = [{ name: 'Customer0', data: [[773,1363709520],[774,1363709580]] }, 
	{ name: 'Customer1', data: [[1546,1363709520],[1548,1363709580]] }];
*/
	$('#container').highcharts('StockChart', {
	    
	    chart: {
	    },
	    
	    navigator: {
	    	enabled: true
	    },
	    
	    rangeSelector: {
	    	selected: 1
	    },
	    
	    series: toChartData  
	    /*   
	    [{
	        name: 'USD to EUR',
	        data: usdeur
	    }]
	    */

	});
}

function getDataForChart(userId)
{
	var sensors = {};
	var sensor_names = new Array();
	var measures = new Array();
	
	getSensors(userId,sensors);
	$.each(sensors, function(index, value)
	{
		getSensorName(value.sensor_type_id,sensor_names,index);
		getMeasures(value.id,measures,index);
	});
	
	var toChartObject = {};

	
	$.each(measures, function(index1, value1)
	{
	
		var toSeriesData = [];
		
		
		$.each(value1, function(index2, value2)
		{
			var data = [];
			
			var date = new Date(value2.timestamp);
			var val = value2.value;
			data[0] = date;
			data[1] = val;
			
			toSeriesData[index2] = data;
			
		
		});
		
		toChartObject[index1] = {"name": sensor_names[index1], "data": toSeriesData};
		toChartData[index1] = toChartObject[index1];
	
	});
//	console.log(toChartData);
	
	drawChart();
		
}

function getSensors(userId,sensors)
{
			var filters = [{"name": "user_id", "op": "equals", "val": userId}];
		
				$.ajax(
					{
					async: false,
					contentType: "application/jsonp",
					url: api_url+"api/sensor",
					data: {"q": JSON.stringify({"filters": filters})},
					crossDomain : true,
					xhrFields: 
						{
					    withCredentials: true
					    },
				    success:
				     function(result) {
				     	$.each(result.objects , function(index, value)
				     	{
				     		sensors[index] = value;
				      	});
			     	
					 },
					 error:
					  function(xhr, textStatus, errorThrown) {
		        		alert(xhr.responseText);	
		        		alert(textStatus);
		        	  }
	
					
					});
}

// maybe it could be async: true
// Check it

function getSensorName(sensorId,name,index)
{
			var filters = [{"name": "id", "op": "equals", "val": sensorId}];
		
				$.ajax(
					{
					async: false,
					contentType: "application/jsonp",
					url: api_url+"api/sensortype",
					data: {"q": JSON.stringify({"filters": filters})},
					crossDomain : true,
					xhrFields: 
						{
					    withCredentials: true
					    },
				    success:
				     function(result) {
				     	name.push(result.objects[0].name);
					 },
					 error:
					  function(xhr, textStatus, errorThrown) {
		        		alert(xhr.responseText);	
		        		alert(textStatus);
		        	  }
	
					
					});

}

function getMeasures(sensorId,measures,index)
{
			var filters = [{"name": "sensor_id", "op": "equals", "val": sensorId}];
		
				$.ajax(
					{
					async: false,
					contentType: "application/jsonp",
					url: api_url+"api/measure?results_per_page=2000",
					data: {"q": JSON.stringify({"filters": filters})},
					crossDomain : true,
					xhrFields: 
						{
					    withCredentials: true
					    },
				    success:
				     function(result) {
				     	measures.push(result.objects);
				    
					 },
					 error:
					  function(xhr, textStatus, errorThrown) {
		        		alert(xhr.responseText);	
		        		alert(textStatus);
		        	  }
	
					
					});
}

