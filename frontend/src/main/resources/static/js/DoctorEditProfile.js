$(document).ready(function () {
  const cookie = document.cookie;
  if (cookie == "") window.location.href = "Auth";

  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  console.log(token);

  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  console.log(urlParams);

  const doctor_id = urlParams.get("doctor_id");

  $.ajax({
    url: `http://localhost:8050/doctor/display/${doctor_id}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $("#dname").val(result.name);
      $("#dage").val(result.age);
      $("#dgender").val(result.gender);
      $("#dphone").val(result.phone);
      $("#demail").val(result.email);
      $("#dspeciality").append(
        `<option value=${result.speciality.id}>${result.speciality.name}</option>`
      );
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        // alert("Some Error Occurred");
        swal("", errorObj.errorMessage, "error", {
          button: "OK",
        });
      }
    },
  });

  $(".pedit-form").on("submit", function (e) {
    e.preventDefault();

    console.log("clicked");

    var doctorData = {
      name: $("#dname").val(),
      email: $("#demail").val(),
      age: $("#dage").val(),
      gender: $("#dgender").val(),
      phone: $("#dphone").val(),
      specialityId: $("#dspeciality").val(),
    };
    console.log(doctorData);

    $.ajax({
      url: `http://localhost:8050/doctor/${doctor_id}`,
      method: "PUT",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(doctorData),
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (response) {
        // Handle successful response
        console.log(response);
        // alert("Updated Successfully!");
        // window.location.href = "EditDoctor";
        swal("Updated Successfully!", "", "success", {
          button: "OK",
        }).then((value) => {
            window.location.href = 'EditDoctor';
          });
      },
      error: function (xhr, status, errorThrown) {
        if (xhr.status == 403) {
          window.location.href = "Auth";
        } else {
          var errorObj;

          if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

          console.log(errorObj);

          // if (errorObj) alert(errorObj.errorMessage);
          // else alert("Some Error Occurred");
          if (errorObj) {
            swal("", errorObj.errorMessage, "error", {
              button: "OK",
            });
          }
          else 
          {
            swal("", "Some Error Occurred", "error", {
              button: "OK",
            });
          }
        }
      },
    });
  });
});
