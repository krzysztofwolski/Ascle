﻿$(document).ready(function() {
	// Function hide button and shows picutres
	$("#showpic_1").on('click',function()
	{
		$('#showpic_1').hide();
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

	

	$('.subpage').hide();
    $('#login').slideDown();
    
    var active = $('#login');
	//collapse
	function changePage(page){
				active.stop().slideUp('slow', function() {
	      page.stop().slideDown();
	      active = page;
	      active.show();
		  });
			};
		
		$('#trustButton').on('click',function () {
	    changePage($('#showData'));
	   	 });
	  	$('#showMagic').click(function () {
	     changePage($("#fun"));
	  	});
	  	$('#logout').click(function () {
	     changePage($("#login"));
	  	});

	  	
});
