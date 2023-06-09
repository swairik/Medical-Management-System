$(document).ready(function () {
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  console.log(urlParams);

  const appointmentDetailsId = urlParams.get("appointmentDetailsId");
  const appointmentId = urlParams.get("appointmentId");

  console.log(appointmentDetailsId);
  const cookie = document.cookie;
  if (cookie == "") window.location.href = "Auth";

  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];

  console.log(token);

  var feedbackData;

  $.ajax({
    url: `http://localhost:8050/appointment/display/${appointmentId}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      console.log(result.appointmentDetails.prescription);

      var diagnosis= "", medication="", tests=""

      var prescriptionString = result.appointmentDetails.prescription

      if(result.appointmentDetails.prescription.length!=0) {
        diagnosis = prescriptionString.substring(prescriptionString.indexOf("Diagnosis=") + ("Diagnosis=").length, prescriptionString.indexOf("Medication"));
        medication = prescriptionString.substring(prescriptionString.indexOf("Medication=") + ("Medication=").length, prescriptionString.indexOf("Tests"));
        tests = prescriptionString.substring(prescriptionString.indexOf("Tests=") + ("Tests=").length);
      } 
      
      $("#pID").text(result.id);
      $("#pname").text(result.patient.name);
      $("#page").text(result.patient.age);
      $("#pgender").text(result.patient.gender);
      $("#pdocname").text(result.doctor.name);
      $("#pdate").text(result.start.substring(0, result.start.indexOf("T")));
      $("#diagnosis").text(diagnosis);
      $("#medication").text(medication);
      $("#tests").text(tests);

      feedbackData=result.appointmentDetails.feedback
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        alert("Some Error Occurred");
      }
    },
  });

  $("#prescription_form").on("submit", function (e) {
    e.preventDefault(); // Prevent default form submission

    var prescriptionData =
      "Diagnosis=" +
      $("#diagnosis").val() +
      "Medication=" +
      $("#medication").val() +
      "Tests=" +
      $("#tests").val();

    console.log(prescriptionData);

    var prescriptionData = {
      prescription: prescriptionData,
      feedback: feedbackData.length==0?null:feedbackData,
      rating: 0.0,
    };

    console.log(prescriptionData);

    // Send Ajax request
    $.ajax({
      url: `http://localhost:8050/appointmentDetails/${appointmentDetailsId}`,
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(prescriptionData),
      success: function (response) {
        // Handle successful response
        console.log(response);
        alert("Prescription Updated Successfully!");
        window.location.href = "YourPatients";
      },
      error: function (xhr, status, error) {
        // Handle error response
        if (xhr.status == 403) {
          // window.location.href = "Auth";
        } else {
          var errorObj;
          if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

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
