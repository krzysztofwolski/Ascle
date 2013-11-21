var api_url = "http://kuchnia.mooo.com:5000/";

function user(id,type,log)
{
this.id=id;
this.type=type;
this.is_log;
}

User = new user(0,0,0);

$(document).ready(function() {
	

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
		
		
		$('#trustButton').on('click',function () {
			var login = $('#login').val();
			var pass = $('#pass').val();
		
			$.ajax({
			   type: "POST",
			   dataType: 'text',
			   url: api_url+"login?callback=?",
			   data: {username: login, password: pass},
			   crossDomain : true,
			   success:
			   	function( result ,status, xhr) {
				
				var json = jQuery.parseJSON(result);
												
				alert(json.message);
				console.log("done");
				
				xhr.getResponseHeader('Set-Cookie');
				
				isAuthenticated();
				console.log(User);
				
				if (User.log=true)
				{
					onLoginSuccessful(User.type);
			    }
			    else
			    {
			    	alert("not logged");
			    }
			   },
			   error:
			   	function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);	
	        	}
			   
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
	   	 
	   	 
	function isAuthenticated()
	{
		var json;
				$.ajax(
				{
				contentType: "application/jsonp",
				url: api_url+"api/stats",
				crossDomain : true,
				xhrFields: 
					{
				    withCredentials: true
				    },
			    success:
			     function(result) {
			     	console.log("yea");
			     
					json = jQuery.parseJSON(result);
										
					User.id = json.user_id;
					User.type = json.type;
					User.log = json.logged_in;
					console.log(User);					
				 },
				 error:
				  function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);
	        	  }

				
				});
	}
	
	function searchUser(){
			
			var pes = $("#user_search").val();
			var filters = [{"name": "pesel", "op": "equals", "val": pes}];
			
			$.ajax(
				{
				url: api_url+"api/user",
				data: {"q": JSON.stringify({"filters": filters})},
				crossDomain : true,
				xhrFields: 
					{
				    withCredentials: true
				    },
			    success:
			     function(result) {
			     	if (result.data.num_results){
			     		alert("no user");
			     	}
			    					
				 },
				 error:
				  function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);
	        	  }

				
				});

	}
	   	    
	
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

