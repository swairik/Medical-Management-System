const constructAppointmentInfo = (index,result) => {
  return `
  <li class="table-row">
  <div class="col col-1" data-label="No">${index+1}</div>
  <div class="col col-2" data-label="Date">${result.scheduleResponse.weekDate}</div>
  <div class="col col-3" data-label="Name">${result.patientResponse.name}</div>
  <div class="col col-4" data-label="Age">${result.patientResponse.age}</div>
  <div class="col col-5" data-label="Prescription"><a id="pres" href="AddPrescription">Add</a></div>
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

  var start_time_id = document.getElementById("start_time");
  var end_time_id = document.getElementById("end_time");

  $("#view_button").click(function (e) {
    const start = new Date($("#start_time").val());

    console.log(start);

    const formattedStartDate = start
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

    console.log(formattedStartDate);

    const end = new Date($("#end_time").val());

    console.log(end);

    const formattedEndDate = end
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
    console.log(formattedEndDate); // Output: 2023/03/19 08:30:00

    $.ajax({
      url: `http://localhost:8050/appointment/display/doctor/${doctor_id}/between?start=${formattedStartDate}&end=${formattedEndDate}`,
      type: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        $.each(result, function (key, value) {
          $(".responsive-table").append(constructAppointmentInfo(key, value));
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
  });
});
