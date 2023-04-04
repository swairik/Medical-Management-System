function getWeekNumber(d) {
  d = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
  d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay() || 7));
  var yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
  var weekNo = Math.ceil(((d - yearStart) / 86400000 + 1) / 7);
  return [d.getUTCFullYear(), weekNo];
}

function getDate(d) {
  var date = d.toJSON().slice(0, 10);
  var nDate =
    date.slice(0, 4) + "-" + date.slice(5, 7) + "-" + date.slice(8, 10);
  return nDate;
}

var curDateTime = new Date();
console.log(curDateTime.getDay());

var result = getWeekNumber(curDateTime);
console.log(result);

console.log(getDate(curDateTime));

$(document).ready(function () {
  const cookie = document.cookie;
  console.log(cookie)
  if(cookie=='') window.location.href = "Auth";

  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
    const doctor_id = cookie
    .split("; ")
    .find((row) => row.startsWith("id="))
    .split("=")[1];
  console.log(token)
  console.log(doctor_id);

  var date_id = document.getElementById("date");
  var start_time_id = document.getElementById("start_time");
  var end_time_id = document.getElementById("end_time");

  date_id.min = getDate(curDateTime);

  date_id.value = getDate(curDateTime);

  start_time_id.value =
    (curDateTime.getHours() < 10 ? "0" : "") +
    curDateTime.getHours() +
    ":" +
    (curDateTime.getMinutes() < 10 ? "0" : "") +
    curDateTime.getMinutes();
  end_time_id.value =
    (curDateTime.getHours() < 10 ? "0" : "") +
    curDateTime.getHours() +
    ":" +
    (curDateTime.getMinutes() < 10 ? "0" : "") +
    curDateTime.getMinutes();

  $.ajax({
    url: `http://localhost:8050/schedule/display/doctor/${doctor_id}/upcomingAll`,
    type: "GET",
    data: {
      stamp: (new Date())
      .toLocaleString("en-US", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false,
      })
      .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6")
    },
    headers: {
      "Authorization": `Bearer ${token}`
    },
    success: function (result) {
      console.log(result);

      $.each(result, function (key, value) {
        console.log(value);

        $(".responsive-table").append(`
            <li class="table-row">
            
            <div class="col col-2" data-label="Date">${value.start.substring(0, value.start.indexOf('T'))}</div>
            
            <div class="col col-4" data-label="StartTime">${
              value.start.substring(value.start.indexOf('T')+1).replace(/:00$/, '')
            }</div>
            <div class="col col-5" data-label="EndTime">${
              value.end.substring(value.end.indexOf('T')+1).replace(/:00$/, '')
            }</div>
            
            <div class="col col-7" data-label="Status">${
              value.approvalStatus ? "Approved" : "Not Appproved"
            }</div>
            <div class="col col-8" data-label="Remove"><button id="remove_button" type="remove" value=${
              value.id
            }>Remove</button></div>
            </li>`);
      });
    },
    error: function (xhr, status, errorThrown) {
      console.log(error);
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        alert("Some Error Occurred");
      }
    },
  });

  $("#save_button").click(function (e) {

    console.log($("#date").val())
    console.log($("#start_time").val())
    console.log($("#end_time").val())

    const modifiedDate = ($("#date").val()).replace(/-/g, '/');

    console.log(modifiedDate)

    const doctorSchedule = 
    {
      "doctorId": doctor_id,
      "startTime":modifiedDate + ' ' + $("#start_time").val() + ':00',
      "endTime": modifiedDate + ' ' + $("#end_time").val() +  ':00'
    }

    console.log("clicked");
    e.preventDefault();
    console.log(doctorSchedule)
    $.ajax({
      url: "http://localhost:8050/schedule/",
      type: "POST",
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(doctorSchedule),
      headers: {
        "Authorization": `Bearer ${token}`
      },
      success: function (result) {
        console.log(result);
        $.each(result, function (key, value) {
          console.log("hi")
        });
        window.location.href = "UpdateSchedule";
      },
      error: function (xhr, status, errorThrown) {
        let errorObj;
        console.log(xhr);

        if (xhr.status == 403) {
          window.location.href = "Auth";
        } else {
          if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

          if (errorObj) alert(errorObj.errorMessage);
          else alert("Some Error Occurred");
        }
      },
    });
  });

  // $(".responsive-table").on("click", "button#remove_button", function (e) {
  //   console.log("clicked");
  //   console.log(this);
  //   e.preventDefault();
  //   $.ajax({
  //     type: "DELETE",
  //     url: `http://localhost:8050/schedule/${this.value}`,
  //     headers: {
  //       "Authorization": `Bearer ${token}`
  //     },
  //     success: function (result) {
  //       alert("Schedule Removed Successfully");
  //       window.location.href = "UpdateSchedule";
  //     },
  //     error: function (xhr, status, errorThrown) {
  //       if (xhr.status == 403) {
  //         window.location.href = "Auth";
  //       } else {
  //         if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

  //         if (errorObj) alert(errorObj.errorMessage);
  //         else alert("Some Error Occurred");
  //       }
  //     },
  //   });
  // });
});
