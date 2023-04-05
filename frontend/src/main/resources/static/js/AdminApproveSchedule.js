const constructScheduleMenu = (key,value) => {
  var div = `
  <li class="table-row">
  <div class="col col-1" data-label="DoctorId">${value.doctor.id}</div>
  <div class="col col-2" data-label="Name">${value.doctor.name}</div>
  <div class="col col-3" data-label="Speciality">${value.doctor.speciality.name}</div>
  <div class="col col-4" data-label="Date">${value.start.substring(0, value.start.indexOf('T'))}</div>
  <div class="col col-6" data-label="StartTime">${value.start.substring(value.start.indexOf('T')+1).replace(/:00$/, '')}</div>
  <div class="col col-7" data-label="EndTime">${ value.end.substring(value.end.indexOf('T')+1).replace(/:00$/, '')}</div>
  <div class="col col-7" data-label="Status">${value.approvalStatus?"Approved":"Not Approved"}</div>
  <div class="col col-8">
    <button class="btn_accept" value=${value.id}>Accept</button>
  </div>
  <div class="col col-9">
    <button class="btn_remove" value=${value.id}>Remove</button>
  </div>
</li>
  `;

  return div;
};



  $(document).ready(function () {

    const cookie = document.cookie;
    if(cookie=='') window.location.href = "Auth";
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  console.log(token);

  console.log((new Date())
  .toLocaleString("en-US", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    hour12: false,
  })
  .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6"))

  $.ajax({
    url: "http://localhost:8050/schedule/display/upcoming",
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
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $.each(result, function (key, value) {
        console.log(value);
        if(!value.approvalStatus){
        $(".responsive-table").append(constructScheduleMenu(key,value));
        }
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
    
    $(".responsive-table").on("click","button.btn_accept",function(e) {
      console.log("clicked")
      console.log(this)
      e.preventDefault();
      $.ajax({
        type: "PUT",
        url: `http://localhost:8050/schedule/${this.value}/approve`,
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function(result) {
          alert('Schedule Approved...')
          window.location.href = 'ApproveSchedule';
        },
        error: function(xhr, status, errorThrown) {
          if (xhr.status == 403) {
            window.location.href = "Auth";
          } else {
            var errorObj;
            if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
    
            if (errorObj) alert(errorObj.errorMessage);
            else alert("Some Error Occurred");
          }
        }
      });
    });

    $(".responsive-table").on("click","button.btn_remove",function(e) {
      console.log("clicked")
      console.log(this)
      e.preventDefault();
      $.ajax({
        type: "DELETE",
        url: `http://localhost:8050/schedule/${this.value}`,
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function(result) {
          alert('Schedule Rejected...')
          window.location.href = 'ApproveSchedule';
        },
        error: function(xhr, status, errorThrown) {
          if (xhr.status == 403) {
            window.location.href = "Auth";
          } else {
            var errorObj;
            if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
    
            if (errorObj) alert(errorObj.errorMessage);
            else alert("Some Error Occurred");
          }
        }
      });
    });
  
  });
