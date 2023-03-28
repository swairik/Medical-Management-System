$(document).ready(function () {
  const cookie = document.cookie;
  if(cookie=='') window.location.href = "Auth";
  
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  
  console.log(token);

  $.ajax({
    url: "http://localhost:8050/speciality/display",
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      docList = result;
      console.log(docList);
      $.each(result, function (key, value) {
        console.log(value);
        // $(".add_doctor_personal").append("Name= " + value.name + "Speciality= " + value.speciality.name);
        $("#dfspeciality").append(
          `<option value=${value.id}>${value.name}</option>`
        );
      });
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        var errorObj;
        if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

        if (errorObj) alert(errorObj.errorMessage);
        else alert("Some Error Occurred");
      }
    },
  });

  $(".dedit-form").submit(function (e) {
    e.preventDefault();

    console.log("clicked");

    var docData = {
      name: $("#dfname").val(),
      gender: $("#dfgender").val(),
      age: $("#dfage").val(),
      email: $("#dfemail").val(),
      phone: $("#dfcontact").val(),
      specialityId: $("#dfspeciality").val(),
    };

    console.log(docData);

    $.ajax({
      url: "http://localhost:8050/doctor/",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(docData),
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        window.location.href = "EditDoctor";
        console.log(result);
      },
      error: function (xhr, status, errorThrown) {
        if (xhr.status == 403) {
          // window.location.href = "Auth";
        } else {
          var errorObj;
          if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

          if (errorObj) alert(errorObj.errorMessage);
          else alert("Some Error Occurred");
        }
      },
    });

  });
});
