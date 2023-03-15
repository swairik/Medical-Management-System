$(document).ready(function () {
  var dataString = $("#patient_form").serialize();
  var dataString2 = $("#login_form").serialize();

  console.log(dataString);
  console.log(dataString2);

  $("#login").click(function (e) {
    console.log("clicked");
    console.log($("#login_form").serialize());
    var loginData = {
      email: $("#email").val(),
      password: $("#password").val(),
    };

    console.log(loginData)

    // $('#name').val()
    // console.log(loginData)
    e.preventDefault();
    $.ajax({
      url: "http://localhost:8050/auth/login",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(loginData),
      success: function (result) {
        // window.location.href = 'EditDoctor';
        console.log(result);
        result.role = "PATIENT";
        document.cookie = `authToken=${result.token}`;
        // if(result.role==="PATIENT") window.location.href = 'Patient';
      },
      error: function (error) {
        console.log(error);
        alert("Wrong credential");
      },
    });
  });

  $("#sign_up").click(function (e) {
    console.log("clicked");

    var patientData = {
      name: $("#name").val(),
      gender: $("#gender").val(),
      age: $("#age").val(),
      email: $("#email_patient").val(),
      phone:$("#phone").val(),
      password: $("#password").val(),
      role: "PATIENT",
    };
    console.log(patientData)

    // $('#name').val()
    // console.log(loginData)
    e.preventDefault();
    $.ajax({
      url: "http://localhost:8050/auth/register",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(patientData),
      success: function (result) {
        // window.location.href = 'EditDoctor';
        console.log(result);
        document.cookie = `authToken=${result.token}`;
        // if(result.role==="PATIENT") window.location.href = 'Patient';
      },
      error: function (error) {
        console.log(error);
        alert("Some error occurred!");
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
