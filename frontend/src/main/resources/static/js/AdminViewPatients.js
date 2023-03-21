const constructPatientInfo = (value) => {
    var div = `<div class="container">
    <img src="../images/doctor-consultation.png" alt=" " />
    <table>
      <tr>
        <td>NAME</td>
        <td>:</td>
        <td id="pname">${value.patientResponse.name}</td>
      </tr>
      <tr>
        <td>AGE</td>
        <td>:</td>
        <td id="page">${value.patientResponse.age}</td>
        <td>GENDER</td>
        <td>:</td>
        <td id="pgender">${value.patientResponse.gender}</td>
      </tr>
      <tr>
        <td>EMAIL</td>
        <td>:</td>
        <td id="pemail">${value.patientResponse.email}</td>
        <td>CONTACT</td>
        <td>:</td>
        <td id="pcontact">${value.patientResponse.phone}</td>
      </tr>
      <tr>
        <td>DOCTOR</td>
        <td>:</td>
        <td id="pdoc">${value.scheduleResponse.doctorResponse.name}</td>
        <td>SPECIALITY</td>
        <td>:</td>
        <td id="pspeciality">${value.scheduleResponse.doctorResponse.speciality.name}</td>
      </tr>
      <tr>
        <td>DATE</td>
        <td>:</td>
        <td id="pdate">${value.scheduleResponse.weekDate}</td>
        <td>SLOT TIME</td>
        <td>:</td>
        <td id="pslottime">${value.scheduleResponse.slotResponse.start}-${value.scheduleResponse.slotResponse.end}</td>
      </tr>
    </table>
  </div>`;
  
    return div;
  };
  

$(document).ready(function () {
    const cookie = document.cookie;
    const token = cookie
      .split("; ")
      .find((row) => row.startsWith("authToken="))
      .split("=")[1];
    console.log(token);
  
    $.ajax({
      url: "http://localhost:8050/appointment/display",
      type: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        docList = result;
        console.log(docList);
        $.each(result, function (key, value) {
          console.log(value);
          // $(".add_doctor_personal").append("Name= " + value.name + "Speciality= " + value.speciality.name);
          $(".patient").append(constructPatientInfo(value));
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
  