//var docList;
//var docMenu = document.getElementById('add_doctor_menu');

const constructDoctorMenu = (value) => {
  var div1=`<img src="../images/AddDoctor.png" alt="An example image" style="margin-left:1%">`

  var div2= `<div style="margin-left:5%;margin-top:2%">
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
        window.location.href = 'EditDoctor';
      },
      error: function(result) {
        alert('error');
      }
    });
  });

});

// jQuery.each( docList, function( i, val ) {
//    $( "#" + i ).append( document.createTextNode( " - " + val ) );
//  });
