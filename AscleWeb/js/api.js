var api_url = "http://kuchnia.mooo.com:5000/";

function user(id,type,log)
{
this.id=id;
this.type=type;
this.is_log=log;

this.getId = function() {
        return this.id;
    };
    
this.getType = function() {
        return this.type;
    };

this.getIsLog = function() {
        return this.is_log;
    };
    
}

User = new user();

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
		
	function doingStuff()
	{
		alert("Iha");
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
										
					User = new user(json.user_id, json.type, json.logged_in);
					/*
					User.id = json.user_id;
					User.type = json.type;
					User.is_log = json.logged_in;
					*/
					console.log(User);	
					console.log(User.getId());					
				 },
				 error:
				  function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);
	        	  }

				
				});
	}
		
		
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
	   	 
	   	 
	   	 // function search user by pesel
	
	function searchUser(){
			
			var pes = $("#user_search").val();
			var filters = [{"name": "pesel", "op": "equals", "val": pes}];
			
			console.log(JSON.stringify({"filters": filters}));
			
			if(!$.trim($("#user_search").val()).length)
			{
				alert("Pole jest puste");
			}
			else
			{
			
			console.log(pes);			
		
				$.ajax(
					{
					contentType: "application/jsonp",
					url: api_url+"api/user",
					data: {"q": JSON.stringify({"filters": filters})},
					crossDomain : true,
					xhrFields: 
						{
					    withCredentials: true
					    },
				    success:
				     function(result) {
				     	
				    	console.log(result.num_results);
						if (result.num_results=='0')
						{
							alert("Nie ma pacjenta o podanym peselu");
						}
						else
						{
							$("#get_first-name").html(result.objects[0].first_name);
							$("#get_last-name").html(result.objects[0].last_name);
							var sex;
							if (result.objects[0].first_name)
							{
								sex = "Mężczyzna";
							}
							else
							{
								sex = "Kobieta";
							}
							$("#get_sex").html(sex);
							$("#get_birth-date").html(result.objects[0].birth_date);
							
						}
					
					 },
					 error:
					  function(xhr, textStatus, errorThrown) {
		        		alert(xhr.responseText);	
		        		alert(textStatus);
		        	  }
	
					
					});
			}
	}
	   	    
	function sendUserDataToSerwer(userData)
	{
		console.log(userData);
		
		$.ajax(
		{
			type: "POST",
			contentType: "application/jsonp",
			url: api_url+"api/user",
			data: JSON.stringify(userData),
			crossDomain : true,
			xhrFields: 
				{
					withCredentials: true
				},
			success:
				function(result) 
				{
				    console.log(result);
				},
			error:
				function(xhr, textStatus, errorThrown) 
				{
		        	alert(xhr.responseText);	
		        	alert(textStatus);
		        	console.log(xhr.responseText);
		        }
		});
	}	   	 
	   	    
	   	    
	   	 //when clicked on "Zaloguj"
	   	    
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
				
				//It's very nasty way to check if user is logged but runninig isAuthenticated() 
				// in the middle of ajax request don't work
				// TO DO in free time
				if (json.message=="Success! Logged in.")
				{
					isAuthenticated();
					onLoginSuccessful(User.getType());
			    }
			    /*
			    else
			    {
			    	alert("not logged");
			    }
			    */
			   },
			   error:
			   	function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);	
	        	}
			   
			});
				        	
		});
	   	    
	   	    // when clicked on "Dodaj pacjenta"
	   	    
	   	    $("#addPatientButton").click(function()
	   	    {
				var login = $("#add_patient_login").val();
				var first_name = $("#add_patient_first_name").val();
				var last_name =$("#add_patient_last_name").val();
				var pesel = $("#add_patient_pesel").val();
				var password = $("#add_patient_password").val();
				var password_comfirm = $("#patient_password_comfirm").val();
				var sex = $('input[name=patient_sex]:checked', '#addPatientForm').val()
				
				if (password != password_comfirm )
				{
					alert("Hasła nie są takie same");
				}
				else
				{
					var patient = {"login": login, "password": password, "last_name": last_name, 
					"first_name": first_name, "sex": sex, "password" : password,
					"pesel": pesel, "type":3};

					sendUserDataToSerwer(patient);
				
				}
				
	   	    	
	   	    });
	   	    
	   	    
	
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
			
			
			$("#searchPatient").click(function(){
				searchUser();
			});
});

