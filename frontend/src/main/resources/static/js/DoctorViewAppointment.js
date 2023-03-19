const constructAppointmentInfo = (result, index) => {
  return `
    <li class="table-row">
          <div class="col col-1" data-label="S. No.">${index + 1}</div>
          <div class="col col-2" data-label="Date">${
            result.scheduleResponse.weekDate
          }</div>
          <div class="col col-3" data-label="Day">${
            result.scheduleResponse.slotResponse.weekday
          }</div>
          <div class="col col-4" data-label="Time">${
            result.scheduleResponse.slotResponse.start
          }-${result.scheduleResponse.slotResponse.end}</div>
          <div class="col col-5" data-label="Patient's Name">${
            result.patientResponse.name
          }</div>
          <div class="col col-6" data-label="Patient's Age">${
            result.patientResponse.age
          }</div>
    </li>
      `;
};

$(document).ready(function () {
  var date = new Date();

  const cookie = document.cookie;
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  const doctor_id = cookie
    .split("; ")
    .find((row) => row.startsWith("id="))
    .split("=")[1];
  console.log(token);
  console.log(doctor_id);


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
    url: `http://localhost:8050/appointment/display/doctor/${doctor_id}/upcoming?stamp=${formattedDate}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $.each(result, function (key, value) {
        $(".responsive-table").append(constructAppointmentInfo(value, key));
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
});
