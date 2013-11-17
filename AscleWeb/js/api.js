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
		
		var api = "http://kuchnia.mooo.com:5000/login";
		$('#trustButton').on('click',function () {
		
			$.ajax({
			   type: "POST",
//			   Access-Control-Allow-Origin: api,
			   dataType: 'text',
			   url: api,
			  // data: {username: 'admin',password: 'admin'},
			   username: 'admin',
			   password: 'admin',
			   crossDomain : true,
			})
			.done(function( data ) {
		    console.log("done");
		    })
		    .fail( function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);		  
	        });

								
		/*
			$.post(api,function(result){
				console.log(result);
				alert( "success" );
				})
				.done(function() {
				    alert( "second success" );
				})
				.fail( function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);		  
	        	})
				.always(function() {
				    alert( "finished" );
				});
				
		    	changePage($('#showData'));
		    	// $("$menu").show();
	   	    */
	   	    
	   	    /*
		   	   $.ajax( {
					url : "http://kuchnia.mooo.com:5000/login",
//					dataType : 'json',
					beforeSend : function(xhr) {
				          var bytes = Crypto.charenc.Binary.stringToBytes("admin" + ":" + "admin");
				          var base64 = Crypto.util.bytesToBase64(bytes);
				          xhr.setRequestHeader("Authorization", "Basic " + base64);
					},
					error : function(xhr, ajaxOptions, thrownError) {
					  reset();
					  onError('Invalid username or password. Please try again.');
					  $('#loginform #user_login').focus();
					},
					success : function(model) {
					  cookies();
				      
					}
				});	
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

