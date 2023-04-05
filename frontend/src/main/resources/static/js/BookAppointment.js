const constructDoctorInfo = (result) => {
  console.log(result);
  return `
    <tr>
    <td>Speciality</td>
    <td>:</td>
    <td id="speciality">${result.speciality.name}</td>
  </tr>
  <tr>
    <td>Name</td>
    <td>:</td>
    <td id="docName">${result.name}</td>
  </tr>
  <tr>
    <td>Contact no.</td>
    <td>:</td>
    <td id="docContact">${result.phone}</td>
  </tr>
  <tr>
    <td>Email</td>
    <td>:</td>
    <td id="DocEmail">${result.email}</td>
  </tr>
    `;
};

const constructSlotMenu = (value) => {
  const date=value.start.substring(0, value.start.indexOf('T')).split('-')
  return `
  <li class="table-row">
    <div class="col col-1" data-label="Date">${date[2]}-${date[1]}-${date[0]}</div>
    <div class="col col-4" data-label="StartTime">${
      value.start.substring(value.start.indexOf('T')+1).replace(/:00$/, '')
    }</div>
    <div class="col col-5" data-label="EndTime">${
      value.end.substring(value.end.indexOf('T')+1).replace(/:00$/, '')
    }</div>
    <div class="col col-6">
    <button type="submit" id="book_slot" value=${value.id} 
    }>Book</button>
    </div>
    </li>
    `;
};

{
  /* <div class="col col-6" data-label="Book">
<button type="submit" id="book_slot" value=${result.slotResponse.id}>Book</button>
</div> */
}

$(document).ready(function () {

  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);

  console.log(urlParams)

  // Get the value of the "id" parameter
  const doctor_id = urlParams.get('id');

  // Use the id value to do something
  console.log(doctor_id); // Output: my-element

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

  const formattedStartDate = (new Date())
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

  console.log(formattedStartDate)

  $.ajax({
    url: `http://localhost:8050/doctor/display/${doctor_id}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $(".DoctorInfo").append(constructDoctorInfo(result));
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        // alert("Some Error Occurred");
        swal("", "Some Error Occurred", "error", {
            button: "OK",
          });
        
      }
    },
  });


  $.ajax({
    url: `http://localhost:8050/schedule/display/doctor/${doctor_id}/upcoming`,
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
      if(result.length===0){
        
      }else{
        $('#NA').replaceWith($('#table_head').show());
        $('.responsive-table').show(); 
      $.each(result, function (key, value) {
        console.log(value);
        $("#slot_menu").append(constructSlotMenu(value));
      });
    }
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        // alert("Some Error Occurred");
        swal("", "Some Error Occurred", "error", {
          button: "OK",
        });
      }
    },
  });

  $("#slot_menu").on("click", "button#book_slot", function (e) {
    e.preventDefault();

    console.log("clicked");
    console.log(this);
    
  

    var appointDetailsData = {
      patientId: patient_id,
      scheduleId: this.value,
      appointmentDetails: {
        prescription: "",
        feedback: "",
        rating: 0
      }
    }

    console.log(appointDetailsData)


    $.ajax({
      type: "POST",
      url: `http://localhost:8050/appointment/`,
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(appointDetailsData),
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        console.log("Booked");
        // alert("Slot Booked");
        // window.location.href = 'EditAppointment';
        swal("Slot Booked...", "", "success", {
          button: "OK",
        }).then((value) => {
            window.location.href = 'EditAppointment';
          });
      },
      error: function (xhr, status, errorThrown) {
        if (xhr.status == 403) {
          window.location.href = "Auth";
        } else {
          if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

          // if (errorObj) alert(errorObj.errorMessage);
          // else alert("Some Error Occurred");
          if (errorObj) {
            swal("", errorObj.errorMessage, "error", {
              button: "OK",
            });
          }
          else 
          {
            swal("", "Some Error Occurred", "error", {
              button: "OK",
            });
          }
        }
      },
    });
  });
});
