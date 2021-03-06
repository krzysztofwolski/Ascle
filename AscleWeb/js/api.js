﻿var api_url = "http://kuchnia.mooo.com:5000/";

var mask=[];
var chartData = [];

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



$(document).ready(function() {

        User = new user();
        $("#test").hide();
        $("#patientList").hide();
        
        $("#drawChart").hide();                

        hideMenu();

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
                                async: false,
                                contentType: "application/jsonp",
                                url: api_url+"api/stats",
                                crossDomain : true,
                                xhrFields: 
                                        {
                                    withCredentials: true
                                    },
                            success:
                             function(result) {
                                     // console.log("yea");
                             
                                        json = jQuery.parseJSON(result);
                                                                                
                                        User = new user(json.user_id, json.type, json.logged_in);
                                        /*
                                        User.id = json.user_id;
                                        User.type = json.type;
                                        User.is_log = json.logged_in;
                                        */
                                        // console.log(User);        
                                        // console.log(User.getId());                                        
                                 },
                                 error:
                                  function(xhr, textStatus, errorThrown) {
                                alert(xhr.responseText);        
                                alert(textStatus);
                          }

                                
                                });
        }

        function loadNumberOfNewMessages(id)
        {
                val = {};
                var filters = [{"name": "receiver_user_id", "op":"equals", "val":id},
                                                {"name":"new", "op":"equals", "val":true}];
                // console.log(JSON.stringify({"filters": filters}));
                $.ajax(
                {
                        async: true,
                        contentType: "application/jsonp",
                        url: api_url+"api/message",
                        data: {"q": JSON.stringify({"filters": filters})},
                        crossDomain : true,
                        xhrFields: 
                        {
                            withCredentials: true
                    },
                    success:
                    function(result) 
                    {
                            if (result.num_results=='0')
                                {
                                        $("#messageLabel").text("Wiadomości");
                                }
                                else
                                {
                                        
                                        var text = result.num_results.toString() + " Wiadomości";
                                        $("#messageLabel").text(text);
                                }
                        },
                        error:
                        function(xhr, textStatus, errorThrown) 
                        {
                            alert(xhr.responseText);        
                            alert(textStatus);
                    }                
                });
        }


        function removeMessage(id)
        {
                if(id != null)
                {
                        $.ajax(
                        {
                                async: false,
                                type: "DELETE",
                                contentType: "application/jsonp",
                                url: api_url+"api/message/" + id,
                                crossDomain : true,
                                xhrFields: 
                                        {
                                                withCredentials: true
                                        },
                                success:
                                        function(result) 
                                        {
                                            alert("Wiadomość usunięta");
                                        },
                                error:
                                        function(xhr, textStatus, errorThrown) 
                                        {
                                        alert(xhr.responseText);        
                                        alert(textStatus);
                                }
                        });
                }
                else
                {
                        console.log("Usuwanie wiadomości zakończone niepowodzeniem")
                }
        }

        function markAsRead(messageData)
        {
                if(messageData != null)
                {
                        messageData.new = false;
                        $.ajax(
                        {
                                type: "PUT",
                                contentType: "application/jsonp",
                                url: api_url+"api/message/" + messageData.id,
                                data: JSON.stringify(messageData),
                                crossDomain : true,
                                xhrFields: 
                                        {
                                                withCredentials: true
                                        },
                                success:
                                        function(result) 
                                        {
                                            // alert("Wiadomość przeczytana");
                                        },
                                error:
                                        function(xhr, textStatus, errorThrown) 
                                        {
                                        alert(xhr.responseText);        
                                        alert(textStatus);
                                }
                        });
                }
        }

        function loadMessages(id)
        {
                val = {};
                var filters = [{"name": "receiver_user_id", "op":"equals", "val":id}];
                $.ajax(
                {
                        async: false,
                        contentType: "application/jsonp",
                        url: api_url+"api/message?results_per_page=150",
                        data: {"q": JSON.stringify({"filters": filters})},
                        crossDomain : true,
                        xhrFields: 
                        {
                            withCredentials: true
                    },
                    success:
                    function(result) 
                    {
                            if (result.num_results=='0')
                                {
                                        alert("Nie ma wiadomości");
                                }
                                else
                                {

                                        $( "#messageList > tbody" ).replaceWith( "<tbody></tbody>" );
                                        $.each(result.objects, function(idx, object)
                                        {
                                                markAsRead(object);
                                                val2 = {};
                                                smarterSearchUser("id", object.sender_user_id, val2);
                                                $("#messageList > tbody").append("<tr>"+
                                                        "<td>"+val2[0].pesel+"</td>"+
                                                        "<td>"+object.text+"</td>"+
                                                        "<td><input class='removeMessageButton' type='button' data-button='"
                                                        + object.id.toString() +"' value='Usuń'/></td></tr>"
                                                );
                                                // console.log(object.text + "added");
                                                loadNumberOfNewMessages(id);
                                        });
                                }
                         },
                        error:
                        function(xhr, textStatus, errorThrown) 
                        {
                            alert(xhr.responseText);        
                            alert(textStatus);
                    }                
                });
        }

        
                
        function onLoginSuccessful(user){
                        var type = user.getType();
                        // console.log("typ: "+type);
                        $("#menu").slideToggle();
                        switch(type)
                        {
                                case 0: 
                                        initAdminPage();
                                        break;
                                case 1:
                                        initDoctorPage(user.getId());
                                        break;
                                case 3:
                                        initPatientPage(user.getId());
                                        break;
                        }
                        changePage($("#home"));
                }
                        
                function initPatientPage(id)
                {
                        var userData={};
                        getUserData(id, userData);
                        // console.log(userData);

                        $("#patient_first-name").html(userData[0].first_name);
                        $("#patient_last-name").html(userData[0].last_name);
                        var sex;
                        if (userData[0].sex)
                        {
                                sex = "Mężczyzna";
                        }
                        else
                        {
                                sex = "Kobieta";
                        }
                        $("#patient_sex").html(sex);
                        $("#patient_birth-date").html(userData[0].birth_date);




                        $("#aboutMeLi").show();
                }
                function initDoctorPage(id)
                {
                        loadNumberOfNewMessages(id);

                        $("#messageBoxLi").show();
                        $("#sendMessageLi").show();
                        $("#addPatientLi").show();
                        $("#patients").show();

                }
                function initAdminPage()
                {
                        $("#sendMessageLi").show();
                        $("#addDoctorLi").show();
                        $("#messageBoxLi").show();
                }        
                
                function hideMenu()
                {
                        $("#patients").hide();
                        $("#addDoctorLi").hide();
                        $("#addPatientLi").hide();
                        $("#aboutMeLi").hide();
                        $("#sendMessageLi").hide();
                        $("#messageBoxLi").hide();
                        $("#menu").hide();

                }

         
                    // function search user by pesel and fill data 
        
        function searchUser(){
                        
                        var pes = $("#user_search").val();
                        var filters = [{"name": "pesel", "op": "equals", "val": pes}];
                        
                        // console.log(JSON.stringify({"filters": filters}));
                        
                        if(!$.trim($("#user_search").val()).length)
                        {
                                alert("Pole jest puste");
                        }
                        else
                        {
                        
                        // console.log(pes);                        
                
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
                                             
                                            // console.log(result.num_results);
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
        
        // becouse we must search user in few places maybe smarter would be return user
        // data and then work with them
        // this function assigns only id but it could return all users data
        // TO DO if someone want...
        
        function smarterSearchUser(searchBy,searchingVal,val)
        {
                        var filters = [{"name": searchBy, "op": "equals", "val": searchingVal}];
                
                                $.ajax(
                                        {
                                        async: false,
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
                                             
                                            if (result.num_results=='0')
                                                {
                                                        alert("Nie ma pacjenta o tych danych");
                                                        val = null;
                                                }
                                                else
                                                {
                                                        val[0] = result.objects[0];        
                                                }
                                        
                                         },
                                         error:
                                          function(xhr, textStatus, errorThrown) {
                                        alert(xhr.responseText);        
                                        alert(textStatus);
                                  }
        
                                        
                                        });
        }
        
        function smartSearch(searchBy,searchingVal,searchingTable,val,isDoubleSearch,searchBy2,searchingVal2)
        {
        				if (isDoubleSearch == null)
        				{
                        	var filters = [{"name": searchBy, "op": "equals", "val": searchingVal}];
                        }
                        else
                        {
                        	var filters = [{"name": searchBy, "op": "equals", "val": searchingVal},{"name": searchBy, "op": "equals", "val": searchingVal}];

                        }
                
                                $.ajax(
                                        {
                                        async: false,
                                        contentType: "application/jsonp",
                                        url: api_url+"api/"+searchingTable,
                                        data: {"q": JSON.stringify({"filters": filters})},
                                        crossDomain : true,
                                        xhrFields: 
                                                {
                                            withCredentials: true
                                            },
                                    success:
                                     function(result) {
                                             
                                            if (result.num_results=='0')
                                                {
                                               //         alert("Nie ma pacjentów");
                                                        val[0] = null;
                                                }
                                                else
                                                {
                                                		$.each(result.objects, function(idx,obj)
                                                		{
                                                        val[idx] = result.objects[idx];    
                                                        });    
                                                }
                                        
                                         },
                                         error:
                                          function(xhr, textStatus, errorThrown) {
                                        alert(xhr.responseText);        
                                        alert(textStatus);
                                  }
        
                                        
                                        });
        }


        function getUserData(id, val)
        {
                $.ajax(
                {
                        async: false,
                        contentType: "application/jsonp",
                        url: api_url+"api/user/"+id,
                        crossDomain : true,
                        xhrFields: 
                                {
                            withCredentials: true
                            },
                    success:
                    function(result) 
                    {
                             // console.log(result);
                            if (result.num_results=='0')
                                {
                                        alert("Nie ma pacjenta o tych danych");
                                }
                                else
                                {
                                        // val[0] = result.objects[0];        
                                        val[0] = result;        
                                }
                        
                         },
                        error:
                          function(xhr, textStatus, errorThrown) 
                          {
                        alert(xhr.responseText);        
                        alert(textStatus);
                }
                });
        }
        


                       
        function sendUserDataToSerwer(userData,id)
        {
                // console.log(userData);
                
                var userToSend={};
                smarterSearchUser("pesel", userData.pesel, userToSend);
                if(userToSend[0] == null)
                        smarterSearchUser("login", userData.login, userToSend);
                else
                        changePage($("#userAlreadyExists"));
                // console.log("Dane pacjenta: ");
                // console.log(userToSend);
                console.log(userToSend);
                if(userToSend[0] == null)
                {
                        $.ajax(
                        {
                        		async: false,
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
                                            alert("Użytkownik dodany");
                                            id[0] = result.id;
                                        },
                                error:
                                        function(xhr, textStatus, errorThrown) 
                                        {
                                        alert(xhr.responseText);        
                                        alert(textStatus);
                                }
                        });
                }
                else
                {
                        changePage($("#userAlreadyExists"));
                }
        }
        
        function addSensor(data,sensorId)
        {
        	            $.ajax(
                        {
                        		async: false,
                                type: "POST",
                                contentType: "application/jsonp",
                                url: api_url+"api/sensor",
                                data: JSON.stringify(data),
                                crossDomain : true,
                                xhrFields: 
                                        {
                                                withCredentials: true
                                        },
                                success:
                                        function(result) 
                                        {
                                      //      alert("Użytkownik dodany");
                                            sensorId[0] = result.id;
                                            console.log(result.id);
                                        },
                                error:
                                        function(xhr, textStatus, errorThrown) 
                                        {
                                        alert(xhr.responseText);        
                                        alert(textStatus);
                                }
                        });

        }
        
        function addMeasure(data)
        {
        	            $.ajax(
                        {
                        	//	async: false,
                                type: "POST",
                                contentType: "application/jsonp",
                                url: api_url+"api/measure",
                                data: JSON.stringify(data),
                                crossDomain : true,
                                xhrFields: 
                                        {
                                                withCredentials: true
                                        },
                                success:
                                        function(result) 
                                        {
                                        
                                        },
                                error:
                                        function(xhr, textStatus, errorThrown) 
                                        {
                                        alert(xhr.responseText);        
                                        alert(textStatus);
                                }
                        });

        }

        
        function sendMessageToSerwer(messageData)
        {
                // console.log(messageData);
                
                $.ajax(
                {
                        type: "POST",
                        contentType: "application/jsonp",
                        url: api_url+"api/message",
                        data: JSON.stringify(messageData),
                        crossDomain : true,
                        xhrFields: 
                                {
                                        withCredentials: true
                                },
                        success:
                                function(result) 
                                {
                                    alert("Wiadomość wysłana");
                                },
                        error:
                                function(xhr, textStatus, errorThrown) 
                                {
                                alert(xhr.responseText);        
                                alert(textStatus);
                        }
                });
        }

        
        function getFormValues(formId,values)
        {
                $.each($(formId).serializeArray(), function(i, field) {
                    values[field.name] = field.value;
                });
        }           
        
        function getFormValues2(formId,values)
        {
                $.each($(formId).serializeArray(), function(i, field) {
                    values[field.name] = field.name;
                });
        }         

        function clearMessageBox()
        {
                $("#toWhom").val("");
                $("#message").val("");
        }
        
        function sendMaskToChartData()
        {
                var val={}; 
                   getFormValues2("#maskButtons",val);
                   
                resetMask();
                
                $.each(val, function(idx, values)
                {
                        mask[values].on = true;
                });
                
           $.getScript("js/charts.js", function(){
                           chooseSeries(mask,chartData);
           });
           
        }
        
        
        function fillUserData(pes)
                        {
                        		
                                var userData = {};
                                
                                smarterSearchUser("pesel",pes,userData);
                                
                                
                                $("#get_first-name").html(userData[0].first_name);
                                $("#get_last-name").html(userData[0].last_name);
                                $("#get_pesel").html(userData[0].pesel);
                                var sex;
                                if (userData[0].sex)
                                {
                                        sex = "Mężczyzna";
                                }
                                else
                                {
                                        sex = "Kobieta";
                                }
                                $("#get_sex").html(sex);
                                $("#get_birth-date").html(userData[0].birth_date);
                                
                                clearMaskButtons();
                                                        
                                $.getScript("js/charts.js", function(){
//                                        drawChart();
                                        mask = [];
                                        console.log(mask);
                                        chartData = [];
                                        getDataForChart(userData[0].id,mask,chartData);
                                        // console.log(chartData);
                                        // 
                                        
                                        $.each(mask, function(idx, val)
                                        {
                                                var check;
                                                // console.log(val.on);
                                                if (val.on == true)
                                                    {
                                                    check="checked"
                                                    }
                                        
                                                var input = $('<input />', {
                                                    'type': 'checkbox',
                                                 //   'id': idx,
                                                    'name': idx,
                                                    'val': true,
                                                    "checked":check
                                                    
                                                });
                                                
                                                var label = $("<label for="+val.name+"  />").text(val.name);
                                                if(val.name != "Lekarz")
                                                {    
	                                                input.appendTo('#seriesChooser');
	                                                label.appendTo('#seriesChooser');        
                                                }
                                                $("#drawChart").show();                                                
                                        });
                                });
                        }    
        
        
        
        
        function resetMask()
        {
                $.each(mask, function(idx, val)
                {
                        mask[idx].on = false;
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
                                // console.log("done");
                                
                                xhr.getResponseHeader('Set-Cookie');
                                
                                //It's very nasty way to check if user is logged but runninig isAuthenticated() 
                                // in the middle of ajax request don't work
                                // TO DO in free time
                                if (json.message=="Success! Logged in.")
                                {
                                        isAuthenticated();
                                        
                                        onLoginSuccessful(User);
                                        $('#loginForm')[0].reset();
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
                
                
                        function addToUserTable(data)
                        {	
                        	
                        						$("#patientList > tbody").append("<tr>"+
                                                        "<td>"+data[0].first_name+" "+data[0].last_name+"</td>"+
                                                        "<td>"+data[0].pesel+"</td>"+
                                                        "<td><input class='showUserData' type='button' data-button='"
                                                        + data[0].pesel.toString() +"' value='Pokaż'/></td></tr>"
                                                );
                        }
                        
                        function clearUserList()
                        {
                        	$( "#patientList > tbody" ).replaceWith( "<tbody></tbody>" );
                        }
                        
                        function clearMaskButtons()
                        {
                        	$( "#seriesChooser" ).replaceWith( "<div id='seriesChooser'></div>" );
                        }

                
                		
                       
                       
                       // when clicked on "Dodaj pacjenta"
                       
                       $("#addPatientButton").click(function()
                       {
                       
                               var val={}; 
                               getFormValues("#addPatientForm",val);
                            var fieldsNotEmpty = true;
                               if (val.password != val.repeatPassword)
                                {
                                        alert("Hasła nie są takie same");
                                        return;
                                }
                                if(val.pesel == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole PESEL jest puste")
                                        return;
                                }
                                if(val.login == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Login jest puste")
                                        return;
                                }
                                if(val.firstName == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Imię jest puste")
                                        return;
                                }
                                if(val.lastName == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Nazwisko jest puste")
                                        return;
                                }
                                if(val.password == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Hasło jest puste")
                                        return;
                                }
                                console.log(val);
                                if(val.patient_sex == null)
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Płec jest puste")
                                        return;
                                }
                                if(val.birthDate == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Data Urodzenia jest puste")
                                        return;
                                }


                                if(fieldsNotEmpty == true)
                                {
                                        var patient= {"login": val.login, "password": val.password, "last_name": val.lastName, 
                                        "first_name": val.firstName, "sex": val.patient_sex, "password" : val.password,
                                        "pesel": val.pesel, "birth_date": val.birthDate+"T00:00:00", "type":3};

                                        
										var patientId = [];
                                        sendUserDataToSerwer(patient,patientId);
                                        $('#addPatientForm')[0].reset();
                                        
                                        var sensor = {"name": "Lekarz"+patientId[0], "sensor_type_id": 102, "user_id": patientId[0]};
                                        
                                        var sensorId = [];
                                        addSensor(sensor,sensorId);
                                        
                                        var measure = {"sensor_id":sensorId[0], "value": User.getId()};
                                        addMeasure(measure);
                                }
                                
                                
                                
                               
                       });
                       
                       $("#addDcotorButton").click(function()
                       {
                               var val={}; 
                               getFormValues("#addDoctorForm",val);
                    
                               var fieldsNotEmpty = true;
                               if (val.password != val.repeatPassword)
                                {
                                        alert("Hasła nie są takie same");
                                        return;
                                }
                                if(val.pesel == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole PESEL jest puste")
                                        return;
                                }
                                if(val.login == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Login jest puste")
                                        return;
                                }
                                if(val.firstName == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Imię jest puste")
                                        return;
                                }
                                if(val.lastName == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Nazwisko jest puste")
                                        return;
                                }
                                if(val.password == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Hasło jest puste")
                                        return;
                                }
                                console.log(val);
                                if(val.sex == null)
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Płec jest puste")
                                        return;
                                }
                                if(val.birthDate == "")
                                {
                                        fieldsNotEmpty = false;
                                        alert("Pole Data Urodzenia jest puste")
                                        return;
                                }

                                if(fieldsNotEmpty == true)
                                {
                                        var doctor = {"login": val.login, "password": val.password, "last_name": val.lastName, 
                                        "first_name": val.firstName, "sex": val.sex, "password" : val.password,
                                        "pesel": val.pesel, "birth_date": val.birthDate+"T00:00:00", "type":1};
        
//                                        console.log(doctor);
										var tmp = [];
                                        sendUserDataToSerwer(doctor,tmp);
                                        $('#addDoctorForm')[0].reset();
                                }
                                
                                

                               
                                                      
                       });
                       
                       
                       $("#sendMessageButton").click(function()
                       {
                               var val={}; 
                               getFormValues("#sendMessageForm",val);
                               var val2 = {};
                               smarterSearchUser("pesel",val.toWhom,val2);
                               
                               messageData = {"sender_user_id" : User.id, "receiver_user_id": val2[0].id, 
                                                          "text": val.message, "new": true};
                               
                               sendMessageToSerwer(messageData);
                               clearMessageBox();
                               changePage($("#messageSent"));
                                                      
                               
                       });
                       
                       $("#test").click(function()
                       {
                               data = { "pesel" : 24680};
                                $.ajax(
                                {
                                        type: "PUT",
                                        contentType: "application/jsonp",
                                        url: api_url+"api/user/188",
                                        data: JSON.stringify(data),
                                        crossDomain : true,
                                        xhrFields: 
                                                {
                                                        withCredentials: true
                                                },
                                        success:
                                                function(result) 
                                                {
                                                    alert("Dodano");
                                                },
                                        error:
                                                function(xhr, textStatus, errorThrown) 
                                                {
                                                alert(xhr.responseText);        
                                                alert(textStatus);
                                        }
                                });
                               
                       });
                        
                        
                        
                        
                        $("#searchPatient").click(function(){
                                var pes = $("#user_search").val();
                                fillUserData(pes);
                        });

                       $('#drawChart').click(function(){
                                  sendMaskToChartData();
                          });

                       $("#messageBox").on(
                               "click",
                               ".removeMessageButton",
                               function(zEvent)
                               {
                                       console.log("Usuwam Wiadomość");
                                       var data = $(this).attr('data-button');
                                       console.log(data);
                                       removeMessage(data);
                                       loadMessages(User.getId());
                                       changePage($("#messageBox"));
                               });
                               
 					   $("#patientList").on(
                               "click",
                               ".showUserData",
                               function(zEvent)
                               {
                                       var data = $(this).attr('data-button');
                                       console.log(data);
                                       fillUserData(data);
                                       $("#patientList").hide();
                               });
                             
                        $("#patientListHide").click(function()
                        {
                        	$("#patientList").hide();
                        });
                               
                        $("#showPatients").click(function()
                        {
                        	clearUserList();
                        	$("#patientList").show();
                        	var sensorsList = [];
                        	smartSearch("sensor_type_id",102,"sensor",sensorsList,null,null,null);
                    //    	console.log(sensorsList );
                        	var measuresList = [];
                        	$.each(sensorsList, function(idx, obj)
                        	{
                        		smartSearch("sensor_id",obj.id,"measure",measuresList,1,"value",User.getId());
                        	//	console.log(measuresList[0]);
                        		if (measuresList[0] != null)
                        		{
                        			userData = [];
                        			smarterSearchUser("id",obj.user_id,userData);
                        			addToUserTable(userData);
                        		//	console.log(userData);
                        			
                        		}
                        	});
                        	
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
         
                          $("#homeLi").click(function()
                          {
                                  changePage($("#home"));
                           });

                       $("#sendMessageLi").click(function(){
                               changePage($("#sendMessage"));
                       });


                          $('#showMagic').click(function () {
                     changePage($("#fun"));
                          });

                          $("#aboutMeLi").click(function(){
                                  changePage($("#showCurrentPatient"));
                          });
                          
                          $('#logout').click(function () {
                     changePage($("#myLogin"));
                     $("$menu").hide();
                        });
                        
                        $("#messageBoxLi").click(function(){
                                loadMessages(User.getId());
                                changePage($("#messageBox"));
                        });
                        
                        $("#logOutLi").click(function(){
                                hideMenu();
                                changePage($("#myLogin"));

                        });
                        $
});