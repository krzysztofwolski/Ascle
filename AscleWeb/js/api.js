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
	
	
	jQuery["postJSON"] = function( url, data, callback ) {
    // shift arguments if data argument was omitted
	    if ( jQuery.isFunction( data ) ) {
	        callback = data;
	        data = undefined;
	    }
	
	    return jQuery.ajax({
	        url: url,
	        type: "POST",
	        contentType:"application/json; charset=utf-8",
	        dataType: "json",
	        data: data,
	        success: callback
	    });
	};
	
	
	
	$('.subpage').hide();
    $('#myLogin').slideToggle();
    
    var active = $('#myLogin');
	//collapse
	function changePage(page){
				active.stop().slideUp('slow', function() {
	      page.stop().slideDown();
	      active = page;
	      active.show();
		  });
			};
		
		
		$('#trustButton').on('click',function () {
		
		var login = $('#login').val();
		var pass = $('#pass').val();	
		var api = "http://kuchnia.mooo.com:5000/login";

		console.log(login);
		console.log(pass);

/*
		$.postJSON(api, {username: 'admin', password: 'admin'}, function (data, status, xhr) {
	    alert('Nailed it!')
		});
		*/
		
		
			$.ajax({
			    type: 'POST',
			    url: api,
			    data: {'login':login  , 'password':pass }, // or JSON.stringify ({name: 'jonas'}),
			    success: function(response, status, xhr){ 
				    var ct = xhr.getResponseHeader("content-type") || "";
				    if (ct.indexOf('html') > -1) {
				      console.log("bbb");
				    }
				    if (ct.indexOf('json') > -1) {
				      console.log("ccc");

				    } 
				},
				error: function(response, status, xhr){ 
					console.log("Error", xhr.statusText);  
				},
			    contentType: "application/json",
			    dataType: 'json'
			});
			
			
			/*
			alert('data: ' + data); 
			    		console.log("aaa");
			    		changePage($('#showData'));
*/
			
		});
	   	 
	   	 
	  	$('#showMagic').click(function () {
	     changePage($("#fun"));
	  	});
	  	$('#logout').click(function () {
	     changePage($("#myLogin"));
	      // $("$menu").hide();

	  	});
	
	
});

