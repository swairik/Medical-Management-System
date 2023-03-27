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

  $.ajax({
    url: `http://localhost:8050/appointment/display/patient/${patient_id}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);

      $.each(result, function (key, value) {
        console.log(value);
        $(".prescription-table").append(
          `<tr>
          <td id="prescription_doctor">${value.scheduleResponse.doctorResponse.name}</td>
          <td id="prescription_speciality">${value.scheduleResponse.doctorResponse.speciality.name}</td>
          <td id="prescription_date">${value.scheduleResponse.weekDate}</td>
          <td id="prescription_status">Added/Not Added</td>
          <td id="prescription_view">
            <button style="background-color: #4caf50" id="prescription_view_btn" value=${value.id} doctor_id=${value.scheduleResponse.doctorResponse.id} patient_id=${value.patientResponse.id}>
            View</button>
          </td>
        </tr>`
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

  $(".prescription-table").on("click", "#prescription_view_btn", function (e) {
    console.log("clicked");
    console.log(this.value);
    console.log($(this).attr("doctor_id"))

    window.location.href = "ViewPrescription?appointment_id=" + this.value + "&patient_id=" + $(this).attr("patient_id") + "&doctor_id=" + $(this).attr("doctor_id") ;
  });


});
