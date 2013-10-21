$(document).ready(function() {
	
	$("#2").on('click',function(){
		console.log("clicked");
		
		var api = "http://kuchnia.mooo.com:5000/api/user/1";
		$.getJSON(api, {'Content-Type': 'application/json'}, function(result)
		{
			$("#showpic_2").text("aaa");
			console.log(result.first_name);
			console.log($(".test").text());
			$(".test").text($(".test").text() + " " + result.first_name);
			
				/*$.each(result, function(i, field)
	    	{
	      		$("div").append(field + " ");
    		});*/
 		});
			
	
	});
	
	
});

