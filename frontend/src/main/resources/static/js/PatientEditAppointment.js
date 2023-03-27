const constructAppointmentInfo = (result) => {
  return `
    <tr>
    <td data-label="DocNmae">${result.scheduleResponse.doctorResponse.name}</td>
    <td data-label="speciality">${result.scheduleResponse.doctorResponse.speciality.name}</td>
    <td data-label="Date">${result.scheduleResponse.weekDate}</td>
    <td data-label="Slot">${result.scheduleResponse.slotResponse.weekday}</td>
    <td data-label="Slot">${result.scheduleResponse.slotResponse.start}-${result.scheduleResponse.slotResponse.end}</td>
    <td data-label="">
        <button class="cancel" value=${result.id}>Cancel</button>
    </td>
  </tr>
      `;
};

$(document).ready(function () {
  var date = new Date();

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

  var formattedDate = date
    .toLocaleString("en-US", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: false,
    })
    .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6");

  console.log(formattedDate);

  $.ajax({
    url: `http://localhost:8050/appointment/display/patient/${patient_id}/upcoming?stamp=${formattedDate}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $.each(result, function (key, value) {
          $("#patient_appointment").append(constructAppointmentInfo(value));
      });
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        alert("Some Error Occurred");
      }
    },
  });

  $("#patient_appointment").on("click", "button.cancel", function (e) {
    console.log("clicked");
    console.log(this);
    e.preventDefault();
    $.ajax({
      type: "DELETE",
      url: `http://localhost:8050/appointment/${this.value}`,
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        alert("Appointment Cancelled");
        window.location.href = "EditAppointment";
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
});
