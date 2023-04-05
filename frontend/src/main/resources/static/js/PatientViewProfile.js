$(document).ready(function () {

  const cookie = document.cookie;
  if(cookie=='') window.location.href = "Auth";
  
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

  var reqs = $.ajax({
    url: `http://localhost:8050/patient/display/${patient_id}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $("#pname").html(result.name);
      $("#page").html(result.age?result.age:'-');
      $("#pgender").html(result.gender?result.gender:'-');
      $("#pcontact").html(result.phone?result.phone:'-');
      $("#pemail").html(result.email);

      $("#patient-name").html(result.name);
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        // alert("Some Error Occurred");
        swal("", "Some Error Occurred", "error", {
          button: "OK",
        });
      }
    },
  });

  console.log(reqs);
});
