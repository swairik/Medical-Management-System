// $(document).ready(function () {
//   $(".pedit").hide();
//   $("#edit").click(function () {
//     $(".pedit").show();
//     $(".card").hide();
//   });
//   $("#edited").click(function () {
//     $(".card").show();
//     $(".pedit").hide();

//     event.preventDefault();

//     var formData = {};
//     $("#patient-form")
//       .serializeArray()
//       .forEach(function (field) {
//         formData[field.name] = field.value;
//       });
//     $.ajax({
//       url: "url/to/api",
//       type: "POST",
//       dataType: "json",
//       data: JSON.stringify(formData),
//       contentType: "application/json",
//       success: function (data) {
//         console.log("Data saved successfully");
//         // var newRow = "<tr>";
//         // newRow += "<td>" + data.id + "</td>";
//         // newRow += "<td>" + data.name + "</td>";
//         // newRow += "<td>" + data.age + "</td>";
//         // newRow += "<td>" + data.gender + "</td>";
//         // newRow += "<td>" + data.address + "</td>";
//         // newRow += "</tr>";
//         //$("#patient-table tbody").append(newRow);
//       },
//       error: function (jqXHR, textStatus, errorThrown) {
//         console.log("AJAX Error: " + textStatus + " - " + errorThrown);
//       },
//     });
//   });

//   $(".container-signup").hide();
//   $("#signup").click(function () {
//     $(".container-signup").show();
//     $(".container-signin").hide();
//   });
//   $("#signin").click(function () {
//     $(".container-signin").show();
//     $(".container-signup").hide();
//   });
// });

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
    <div class="col col-1" data-label="Year">${result.year}</div>
    <div class="col col-2" data-label="Week">${result.week}</div>
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
  $.ajax({
    url: "http://localhost:8050/schedule/display/patient/approved/1",
    type: "GET",
    success: function (result) {
      console.log(result);
      $(".DoctorInfo").append(constructDoctorInfo(result[0]));
      $.each(result, function (key, value) {
        console.log(value);
        // $(".add_doctor_personal").append("Name= " + value.name + "Speciality= " + value.speciality.name);
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
