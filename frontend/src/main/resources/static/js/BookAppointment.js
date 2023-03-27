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

const constructSlotMenu = (result) => {
  return `
  <li class="table-row">
    <div class="col col-1" data-label="Date">${result.weekDate}</div>
    <div class="col col-3" data-label="Day">${result.slotResponse.weekday}</div>
    <div class="col col-4" data-label="StartTime">${result.slotResponse.start}</div>
    <div class="col col-5" data-label="EndTime">${result.slotResponse.end}</div>
    <div class="col col-6">
    <button type="submit" id="book_slot" value=${result.slotResponse.id}>Book</button>
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
        alert("Some Error Occurred");
      }
    },
  });

  $.ajax({
    url: `http://localhost:8050/schedule/display/approved/${doctor_id}`,
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      $.each(result, function (key, value) {
        console.log(value);
        $("#slot_menu").append(constructSlotMenu(value));
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

  $("#slot_menu").on("click", "button#book_slot", function (e) {
    console.log("clicked");
    console.log(this);
    var appointData = {
      patientId: patient_id,
      slotId: this.value,
    };
    console.log(appointData)
    e.preventDefault();
    $.ajax({
      type: "POST",
      url: `http://localhost:8050/appointment/`,
      dataType: "json",
      contentType: "application/json",
      data: JSON.stringify(appointData),
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        console.log("Booked");
        alert("Slot Booked");
        window.location.href = 'EditAppointment';
      },
      error: function (xhr, status, errorThrown) {
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
});
