$(document).ready(function () {
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  console.log(urlParams);

  
  const appointmentDetailsId = urlParams.get("appointmentDetailsId");
  
  console.log(appointmentDetailsId);
  const cookie = document.cookie;
  if(cookie=='') window.location.href = "Auth";
  
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];

    console.log(token)

  $("#prescription_form").on("submit", function (e) {
    e.preventDefault(); // Prevent default form submission

    var prescriptionData = {
      appointmentId: appointmentDetailsId,
      contents: {
        medication: $('#medication').val(),
        test: $('#tests').val(),
        diagnosis: $('#diagnosis').val()
      },
    };

    console.log(prescriptionData);

    // Send Ajax request
    $.ajax({
      url: `http://localhost:8050/prescription/`,
      method: 'POST',
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

          if (errorObj) alert(errorObj.errorMessage);
          else alert("Some Error Occurred");
        }
      },
    });
  });
});
