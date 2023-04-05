const constructAppointmentInfo = (result) => {
  return `
    <tr>
    <td data-label="DocNmae">${result.doctor.name}</td>
    <td data-label="speciality">${result.doctor.speciality.name}</td>
    <td data-label="Date">${result.start.substring(0, result.start.indexOf('T'))}</td>
    <td data-label="Slot">${result.start.substring(result.start.indexOf('T')+1).replace(/:00$/, '')}</td>
    <td data-label="">
        <button class="cancel" value=${result.id} doctor_id=${result.doctor.id}>Cancel</button>
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
      if(result.length===0){
        
      }else{
        $('#NA').replaceWith($('#Table_header').show());
        $("#Table_header").append(`
        <caption>Upcoming Appointment Details</caption>
    <thead>
      <tr>
        <th scope="col">Doctor's Name</th>
        <th scope="col">Speciality</th>
        <th scope="col">Date</th>
        <th scope="col">Slot Time</th>
        <th scope="col">Cancel Appointment</th>
      </tr>
    </thead>`)
      $.each(result, function (key, value) {
          $("#patient_appointment").append(constructAppointmentInfo(value));
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

  $("#patient_appointment").on("click", "button.cancel", function (e) {
    console.log("clicked");
    console.log(this);
    console.log($(this).attr("doctor_id"))

    var doctor_id=$(this).attr("doctor_id")

    e.preventDefault();
    $.ajax({
      url: `http://localhost:8050/appointment/${this.value}?stamp=${(new Date())
      .toLocaleString("en-US", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false,
      })
      .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6")}`,
      type: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        // alert("Appointment Cancelled");
        // window.location.href = "BookAppointment?id=" + doctor_id;
        swal("Appointment Cancelled!", "", "warning", {
          button: "OK",
        }).then((value) => {
            window.location.href = "BookAppointment?id=" + doctor_id;
          });
      },
      error: function (xhr, status, errorThrown) {
        if (xhr.status == 403) {
          window.location.href = "Auth";
        } else {
          var errorObj;
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
