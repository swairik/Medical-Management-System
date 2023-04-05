$(document).ready(function () {
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  console.log(urlParams);

  const appointmentDetailsId = urlParams.get("appointmentDetailsId");
  const appointmentId = urlParams.get("appointmentId");

  const cookie = document.cookie;
  if (cookie == '') window.location.href = "Auth";

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
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        alert("Some Error Occurred");
      }
    },
  });
});
