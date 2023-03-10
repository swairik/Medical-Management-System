//var docList;
//var docMenu = document.getElementById('add_doctor_menu');

const constructDoctorMenu = (value) => {
  var div1=`<img src="../images/AddDoctor.png" alt="An example image" style="margin-left:1%">`

  var div2= `<div style="margin-left:5%">
  Name:  ${value.name} 
  <br/> Age:   ${value.age}
  <br/> Contact:   ${value.phone}
  <br/> Email: ${value.email}
  <br/> Speciality:   ${value.speciality.name}
  </div>`

  var div3 = 
  `<div style="display:flex;background-color:#DDDDEC;">` +
  div1+div2+
  `<div style="margin-left:50%; margin-top:5%">
  <button>
      Edit
  </button>
  <button class="add_doctor_menu_btn_remove" value=${value.id}>
      Remove
  </button>

  </div>` +
  `</div> </br> </br>
`;

  return div3;
};

$(document).ready(function () {
  $.ajax({
    url: "http://localhost:8050/doctor/display",
    type: "GET",
    success: function (result) {
      console.log(result);
      docList = result;
      console.log(docList);
      $.each(result, function (key, value) {
        console.log(value);
        // $(".add_doctor_personal").append("Name= " + value.name + "Speciality= " + value.speciality.name);
        $(".add_doctor_menu").append(constructDoctorMenu(value));
      });
    },
    error: function (error) {
      console.log(error);
    },
  });
  $(".add_doctor_menu").on("click","button.add_doctor_menu_btn_remove",function(e) {
    console.log("clicked")
    console.log(this)
    e.preventDefault();
    $.ajax({
      type: "DELETE",
      url: `http://localhost:8050/doctor/${this.value}`,
      // data: {
      //   id: $("#button_1").val(),
      //   access_token: $("#access_token").val()
      // },
      success: function(result) {
        window.location.href = 'AddDoctor';
      },
      error: function(result) {
        alert('error');
      }
    });
  });

  var docData = {
    "name": "Doc1",
    "gender": "M",
    "age": 35,
    "email": "test20@gmail.com",
    "phone": "123465689",
    "specialityId": 1
  }

  $(".addnew").click(function(e) {
    console.log("clicked")
    e.preventDefault();
    $.ajax({
      url: "http://localhost:8050/doctor/",
      type: "POST",
      dataType: 'json',
      contentType: 'application/json',
      data: JSON.stringify(docData),
      success: function (result) {
        window.location.href = 'AddDoctor';
        console.log(result);
      },
      error: function (error) {
        console.log(error);
      },
    });
   
  });
});

// jQuery.each( docList, function( i, val ) {
//    $( "#" + i ).append( document.createTextNode( " - " + val ) );
//  });