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
	
	$("#menu > #patients").hide();
	$("#menu > #addDoctorLi").hide();
	$("#menu > #addPatientLi").hide();
	$("#menu").hide();

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
			   dataType: 'text',
			   url: api,
			  // data: {username: 'admin',password: 'admin'},
			   username: 'admin',
			   password: 'admin',
			   crossDomain : true,
			})
			.done(function( data ) {
		    console.log("done");
		    changePage($('#showData'));


		    })
		    .fail( function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);		  
	        	});
		     });

		function onLoginSuccessful(type){
			$("#menu").slideToggle();

			switch(type)
			{
				case 0: 
					$("#menu > #addDoctorLi").slideToggle();
					break;
				case 1:
					$("#menu > #addPatientLi").slideToggle();
					$("#menu > #patients").slideToggle();
					break;
			}
			


		}

								
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
				});	*/
							
	   	   
			$('#addDoctorLi').click(function(){
	  			changePage($("#addDoctor"));
		  	});
		  	$('#addPatientLi').click(function()
		  	{
		  		changePage($("#addPatient"));
		  	});
		  	$("#patients").click(function(){
		  		changePage($("#showData"));
		  	});
	 
		  	$("#home").click(function()
		  	{
		  		changePage(active);
	 	  	});
	   	    
		  	$('#showMagic').click(function () {
		     changePage($("#fun"));
		  	});
		  	
		  	$('#logout').click(function () {
		     changePage($("#myLogin"));
		      // $("$menu").hide();
			});
});

