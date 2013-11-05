$(document).ready(function() {
	// Function hide button and shows picutres
	// $("$menu").hide();
	$("#showpic_1").on('click',function()
	{
		console.log( "hide" );

		$('#showpic_1').slideToggle();
		var api = "http://api.flickr.com/services/feeds/photos_public.gne?jsoncallback=?";
		$.getJSON(api,	
		{
		    tags: "mount rainier",
		    tagmode: "any",
		    format: "json"
	    }).done(function( data ) 
    	{
  			$.each( data.items, function( i, item ) 
  			{
		        $( "<img>" ).attr( "src", item.media.m ).appendTo( "#images" );
		        if ( i === 3 ) 
		        {
		        	return false;
       	 		}
      		});
		});

		
	});
	
	// Function should show data form server
	$("#showpic_2").on('click',function()
	{
		var api = "http://kuchnia.mooo.com:5000/api/user";
		$.getJSON(api, {'Content-Type': 'application/json'}, function(result)
		{
			$("#showpic_2").text("aaa");
			console.log("aaa");
	    	/*$.each(result, function(i, field)
	    	{
	      		$("div").append(field + " ");
    		});*/
 		});
	});

	

	


		  	
});

