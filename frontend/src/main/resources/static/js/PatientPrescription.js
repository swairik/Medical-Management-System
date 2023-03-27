$(document).ready(function () {
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  console.log(urlParams);

  const doctor_id = urlParams.get("doctor_id");
  const appointment_id = urlParams.get("appointment_id");
  const patient_id = urlParams.get("patient_id");

  console.log(appointment_id);
  const cookie = document.cookie;
  if(cookie=='') window.location.href = "Auth";
  
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];

 console.log(token)

  // Send Ajax request
  $.ajax({
    url: `http://localhost:8050/prescription/display/${doctor_id}/${patient_id}/${appointment_id}`,
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (response) {
      // Handle successful response
      console.log(response);
      alert("Prescription Fetched Successfully!");
      $('#diagnosis').val(response.contents.diagnosis)
      $('#tests').val(response.contents.test)
      $('#medication').val(response.contents.medication)
    },
    error: function (xhr, status, error) {
      // Handle error response
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
