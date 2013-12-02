var toChartData = [];

var toChartObject = {};
var toSeriesData = [];

var date;

var data = [];

var tmp=0;

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

function chooseSeries(mask,data)
{
	console.log("maska: ")
	console.log(mask);
	
	$.each(data, function(idx, val)
	{
		toChartData[idx] = val;
	});
	aplyMask(mask);
	
	console.log(toChartData);
	
	drawChart();
}

function getDataForChart(userId,mask,chartData)
{
//	mask = [];
	toChartData = [];
	var sensors = {};

	var counter=0;
	var names = [];
	
	console.log(sensors);
	
	getSensors(userId,sensors);
	$.each(sensors, function(index, value)
	{
	
		var sensor_names = new Array();
    	var measures = new Array();
	
    	toSeriesData = [];
	
		getSensorName(value.sensor_type_id,sensor_names);
		getMeasures(value.id,1,index,sensor_names[0]);
		names[index] = sensor_names[0];
		counter += 1;
	
	});

	initMast(mask,counter,names);
	aplyMask(mask);
	drawChart();
	
	$.each(toChartData, function(idx, val)
	{
		chartData[idx] = val;
	});	
		
}

function initMast(mask,len,names)
{
	mask[0] = {"on":true, "name":names[0]};
	for (var i=1; i<len; i++)
	{
		mask[i] = {"on":false, "name":names[i]};
	}
	
	if (mask[0].name == "Lekarz")
	{
		mask[0].on = false;
		mask[1].on = true;
	}
	
}

function aplyMask(mask)
{
	$.each(toChartData, function(idx, val)
    {
    	val.visible = mask[idx].on;
    });
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

function getSensorName(sensorId,name)
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

function getMeasures(sensorId,pageNr,index1,sensorName)
{
			var filters = [{"name": "sensor_id", "op": "equals", "val": sensorId}];
		
				$.ajax(
					{
					async: false,
					contentType: "application/jsonp",
					url: api_url+"api/measure?results_per_page=10?page=pageNr", //
					data: {"q": JSON.stringify({"filters": filters})},
					crossDomain : true,
					xhrFields: 
						{
					    withCredentials: true
					    },
				    success:
				     function(result) {

                tmp=0;
               
                $.each(result.objects, function(index2, value2)
                {
                  data = [];
                  
                  date = Date.parse(value2.timestamp);
                  /*
                  if (tmp == date)
                  {
                    date += 1;
                  }
                  
                  tmp = date;
                        */
                        /*
                  if (date != tmp)
                  {      
                  
                  
*/
                  data[0] = date;
                  data[1] = value2.value;
                  
                  toSeriesData[index2+(pageNr-1)*10] = data;
                  
                  /*
                  }

                  
                  tmp = date;
                  */
                
                });
                
                
               if (pageNr < result.total_pages)
               {
                  pageNr += 1;
                  getMeasures(sensorId,pageNr,index1,sensorName)
               }
               else
               {
                toChartObject[index1] = {"name": sensorName, "data": toSeriesData, visible:false};
                toChartData[index1] = toChartObject[index1];
//                console.log(toChartData);
               }
				   
			     	
				    
					 },
					 error:
					  function(xhr, textStatus, errorThrown) {
		        		alert(xhr.responseText);	
		        		alert(textStatus);
		        	  }
	
					
					});
}

