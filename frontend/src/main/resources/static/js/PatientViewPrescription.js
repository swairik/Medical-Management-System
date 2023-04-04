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
      if(result.length===0){

      }else{
      $('#NA').replaceWith($('.prescription-table').show());
      $(".prescription-table").append(
        `<th>Doctor Name</th>
        <th>Speciality</th>
        <th>Date</th>
        <th>Status</th>
        <th></th>`
      );
      $.each(result, function (key, value) {
        console.log(value);
        var dateParts = value.start.substring(0, value.start.indexOf('T')).split("-");
        $(".prescription-table").append(
          `<tr>
          <td id="prescription_doctor">${value.doctor.name}</td>
          <td id="prescription_speciality">${value.doctor.speciality.name}</td>
          <td id="prescription_date">${dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0]}</td>
          <td id="prescription_view">
            <button   style="${value.appointmentDetails.prescription.length==0?"background-color:grey":"background-color:#4caf50"}" id="prescription_view_btn" 
            value=${value.id} appointmentDetailsId=${value.appointmentDetails.id} ${value.appointmentDetails.prescription.length == 0?'disabled':''}>
            View</button>
          </td>
        </tr>`
        );
      });
    }
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

    window.location.href = "ViewPrescription?appointmentId=" + this.value + "&appointmentDetailsId=" + $(this).attr("appointmentDetailsId");
  });

//   style=${value.appointmentDetails.prescription.length==0?"background-color: grey":"background-color: #4caf50"}

// style="background-color: grey"


});
