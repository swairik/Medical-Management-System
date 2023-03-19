
const constructDoctorInfo = (result) => {
  console.log(result);
  return `
    <tr>
    <td>Speciality</td>
    <td>:</td>
    <td id="speciality">${result.doctorResponse.speciality.name}</td>
  </tr>
  <tr>
    <td>Name</td>
    <td>:</td>
    <td id="docName">${result.doctorResponse.name}</td>
  </tr>
  <tr>
    <td>Contact no.</td>
    <td>:</td>
    <td id="docContact">${result.doctorResponse.phone}</td>
  </tr>
  <tr>
    <td>Email</td>
    <td>:</td>
    <td id="DocEmail">${result.doctorResponse.email}</td>
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

{/* <div class="col col-6" data-label="Book">
<button type="submit" id="book_slot" value=${result.slotResponse.id}>Book</button>
</div> */}

$(document).ready(function () {

  var date=new Date()
  
  var formattedDate = date.toLocaleString('en-US', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, '$3/$1/$2 $4:$5:$6');

  console.log(formattedDate)

  $.ajax({
    url: "http://localhost:8050/doctor/display/1",
    type: "GET",
    success: function (result) {
      console.log(result);
      $(".DoctorInfo").append(constructDoctorInfo(result));
    },
    error: function (error) {
      console.log(error);
    },
  });

  $.ajax({
    url: "http://localhost:8050/schedule/display/patient/approved/1",
    type: "GET",
    success: function (result) {
      console.log(result);
      $.each(result, function (key, value) {
        console.log(value);
        $("#slot_menu").append(constructSlotMenu(value));
      });
    },
    error: function (error) {
      console.log(error);
    },
  });

  $("#slot_menu").on("click","button#book_slot",function(e) {
    console.log("clicked")
    console.log(this)
    var appointData = 
        {
            "patientId": 1,
            "slotId": this.value
        }
    e.preventDefault();
    $.ajax({
      type: "POST",
      url: `http://localhost:8050/appointment/`,
      dataType: 'json',
    contentType: 'application/json',
    data: JSON.stringify(appointData),
      
      success: function(result) {
        console.log(result)
        console.log("Booked")
        alert('Slot Booked');
        // window.location.href = 'Patient';
      },
      error: function(result) {
        alert('Some Error has occurred');
      }
    });
  });
});
