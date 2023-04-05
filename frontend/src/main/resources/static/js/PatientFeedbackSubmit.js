$(document).ready(function () {
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  console.log(urlParams);

  const appointmentId = urlParams.get("appointmentId");
  const appointmentDetailsId = urlParams.get("appointmentDetailsId");

  const cookie = document.cookie;
  if (cookie == "") window.location.href = "Auth";

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

  var prescriptionData;

  $.ajax({
    url: `http://localhost:8050/appointment/display/${appointmentId}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);

      prescriptionData = result.appointmentDetails.prescription;
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        alert("Some Error Occurred");
      }
    },
  });

  $("#feedback").on("submit", function (e) {
    e.preventDefault(); // Prevent default form submission

    var feedbackData =
      "Question1=" +
      $("#Question1").val() +
      "Question2=" +
      $("#Question2").val() +
      "Question3=" +
      $("#Question3").val();

    console.log("check");

    var feedback = {
      prescription: (prescriptionData.lenght == 0) ? null : prescriptionData,
      feedback: feedbackData,
      rating: $(".rating input[name='star']:checked").val()
    };

    console.log(feedback);

    $.ajax({
        url: `http://localhost:8050/appointmentDetails/${appointmentDetailsId}`,
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(feedback),
        success: function (response) {
          // Handle successful response
          console.log(response);
          alert("Feedback Updated Successfully!");
          window.location.href = "Patient";
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
});
