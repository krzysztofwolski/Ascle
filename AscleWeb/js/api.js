var api_url = "http://kuchnia.mooo.com:5000/";

function user(id,type)
{
this.id=id;
this.type=type;
}

User = new user(0,0);

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
		
		
		$('#trustButton').on('click',function () {
			var login = $('#login').val();
			var pass = $('#pass').val();
		
			$.ajax({
			   type: "POST",
			   dataType: 'text',
			   url: api_url+"login",
			   username: login,
			   password: pass,
			   crossDomain : true,
			})
			.done(function( data ) {
				
				console.log("cliked");
				
				if (isAuthenticated())
				{
									    
			    changePage($('#showData'));
			    }


		    })
		    .fail( function(xhr, textStatus, errorThrown) {
	        		alert(xhr.responseText);	
	        		alert(textStatus);		  
	        });

										
	   	 });
	   	 
	function isAuthenticated()
	{
				$.ajax(
				{
				url: api_url+"api/stats"
				
				
				})
				.done(function(result) {
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

