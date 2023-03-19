$(document).ready(function () {
  var dataString = $("#patient_form").serialize();
  var dataString2 = $("#login_form").serialize();

  console.log(dataString);
  console.log(dataString2);

  $("#login").click(function (e) {
    console.log("clicked");

    e.preventDefault();
    
    var loginData = {
      email: $("#email").val(),
      password: $("#password").val(),
    };

    console.log(loginData)

    $.ajax({
      url: "http://localhost:8050/auth/login",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(loginData),
      success: function (result) {
        // window.location.href = 'EditDoctor';
        document.cookie = `authToken=${result.token}`;
        document.cookie = `id=${result.id}`;
      
        if(result.role==="PATIENT") window.location.href = 'Patient';
        if(result.role==="ADMIN") window.location.href = 'Admin';
        if(result.role==="DOCTOR") window.location.href = 'Doctor';
      },
      error: function (error) {
        console.log(error);
        alert("Wrong credential");
      },
    });
  });

  $('#patient_form').on('submit', function(e) {
    e.preventDefault();

    console.log("clicked");

    var patientData = {
      name: $("#name").val(),
      email: $("#email_patient").val(),
      password: $("#passwordpf").val(),
      role: "PATIENT",
    };
    console.log(patientData)

    $.ajax({
      url: "http://localhost:8050/auth/register",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(patientData),
      success: function (result) {
        // window.location.href = 'EditDoctor';
        console.log(result);
        alert('Account Created')
        // if(result.role==="PATIENT") window.location.href = 'Patient';
      },
      error: function (xhr,status,errorThrown) {
        if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

        if (errorObj) alert(errorObj.errorMessage);
        else alert("Some Error Occurred");
      },
    });
  });

  $(".container-signup").hide();
  $("#signup").click(function () {
    $(".container-signup").show();
    $(".container-signin").hide();
  });
  $("#signin").click(function () {
    $(".container-signin").show();
    $(".container-signup").hide();
  });
});
