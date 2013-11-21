var api_url = "http://kuchnia.mooo.com:5000/";

function user(id,type)
{
this.id=id;
this.type=type;
}

User = new user(0,0);

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
			   url: api_url+"login",
			   data: {username: login, password: pass},
			   crossDomain : true,
			   success:
			   	function( data ,status, xhr) {
				console.log(data);
				
				console.log("done");
				
				xhr.getResponseHeader('Set-Cookie');
				
				if (isAuthenticated())
				{
									    
			    changePage($('#showData'));
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
				$.ajax(
				{
				url: api_url+"api/stats",
				xhrFields: 
				{
			    withCredentials: true
			    }
				
				})
				.done(function(result) {
					console.log(result);
					return result.data.is_authenticated;
					User.id = result.data.user_id;
					User.type = result.data.type;
				
				})
				.fail( function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);		  
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

