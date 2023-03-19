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

    
  var start_time_id = document.getElementById("start_time");
  var end_time_id = document.getElementById("end_time");

  $("#view_button").click(function (e) {


    const start = new Date($("#start_time").val());

    console.log(start)

    const formattedStartDate = start.getFullYear() + '/' + ('0' + (start.getMonth() + 1)).slice(-2) + '/' + ('0' + start.getDate()).slice(-2) + ' ' + ('0' + start.getHours()).slice(-2) + ':' + ('0' + start.getMinutes()).slice(-2) + ':' + ('0' + start.getSeconds()).slice(-2);
    console.log(formattedStartDate); 

    const end = new Date($("#end_time").val());

    console.log(end)

    const formattedEndDate = end.getFullYear() + '/' + ('0' + (end.getMonth() + 1)).slice(-2) + '/' + ('0' + end.getDate()).slice(-2) + ' ' + ('0' + end.getHours()).slice(-2) + ':' + ('0' + end.getMinutes()).slice(-2) + ':' + ('0' + end.getSeconds()).slice(-2);
    console.log(formattedEndDate); // Output: 2023/03/19 08:30:00


    $.ajax({
        url: `http://localhost:8050/appointment/display/doctor/${doctor_id}/between?start=${formattedStartDate}&end=${formattedEndDate}}`,
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
  })
  
   
  });
  