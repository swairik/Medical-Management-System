//var docList;
//var docMenu = document.getElementById('add_doctor_menu');
const constructDoctorMenu = (value) => {
  var div1 = `<div class="container">
  <img src="../images/AddDoctor.png" alt=" ">
  <table>
      <tr>
        <td>NAME</td>
        <td>:</td>
        <td>${value.name} </td>
        <td></td>
      <td></td>
      <td>
      <button class="add_doctor_menu_btn_remove" value=${value.id}>
        Remove
    </button>
    </td>
      </tr>
      <tr>
        <td>AGE</td>
        <td>:</td>
        <td>${value.age}</td>
        <td>GENDER</td>
        <td>:</td>
        <td>${value.gender}</td>
      </tr>
      <tr>
        <td>CONTACT</td>
        <td>:</td>
        <td>${value.phone}</td>
      </tr>
      <tr>
        <td>EMAIL</td>
        <td>:</td>
        <td> ${value.email}</td>
      </tr>
      <tr>
        <td>SPECIALITY</td>
        <td>:</td>
        <td> ${value.speciality.name}</td>
      </tr>
    </table>
</div>`;

  return div1;
};


$(document).ready(function () {
  const cookie = document.cookie;
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  console.log(token);

  $.ajax({
    url: "http://localhost:8050/doctor/display",
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
        $(".add_doctor_menu").append(constructDoctorMenu(value));
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
  $(".add_doctor_menu").on("click","button.add_doctor_menu_btn_remove",function(e) {
    console.log("clicked")
    console.log(this)
    e.preventDefault();
    $.ajax({
      type: "DELETE",
      url: `http://localhost:8050/doctor/${this.value}`,
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function(result) {
        alert('Doctor Removed Successfully...')
        window.location.href = 'EditDoctor';
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


