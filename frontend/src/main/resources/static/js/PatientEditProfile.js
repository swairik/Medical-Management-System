$(document).ready(function () {
  const cookie = document.cookie;
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  const patient_id = cookie
    .split("; ")
    .find((row) => row.startsWith("id="))
    .split("=")[1];
  console.log(token);
  console.log(patient_id);

  $.ajax({
    url: `http://localhost:8050/patient/${patient_id}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $("#pname").html(result.name);
      $("#page").html(result.age);
      $("#pgender").html(result.gender);
      $("#pcontact").html(result.phone);
      $("#pemail").html(result.email);
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        alert("Some Error Occurred");
      }
    },
  });

  $("#patient_form").on("submit", function (e) {
    e.preventDefault();

    console.log("clicked");

    var patientData = {
      name: $("#pname").val(),
      email: $("#pemail").val(),
      age: $("#page").val(),
      gender: $("#pgender").val(),
      phone: $("#pcontact").val(),
    };
    console.log(patientData);

    $.ajax({
      url: `http://localhost:8050/patient/${patient_id}`,
      method: "PUT",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(patientData),
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (response) {
        // Handle successful response
        console.log(response);
        alert("Updated Successfully!");
        window.location.href = "PatientProfile";
      },
      error: function (xhr, status, errorThrown) {
        if (xhr.status == 403) {
          window.location.href = "Auth";
        } else {
          if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

          if (errorObj) alert(errorObj.errorMessage);
          else alert("Some Error Occurred");
        }
      },
    });
  });
});