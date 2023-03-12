const constructSpecialityMenu = (value) => {
//     var div1=`<img src="../images/AddDoctor.png" alt="An example image" style="margin-left:1%">`
  
//     var div2= `<div style="margin-left:5%">
//     Name:  ${value.name} 
//     <br/> Age:   ${value.age}
//     <br/> Contact:   ${value.phone}
//     <br/> Email: ${value.email}
//     <br/> Speciality:   ${value.speciality.name}
//     </div>`
  
//     var div3 = 
//     `<div style="display:flex;background-color:#DDDDEC;">` +
//     div1+div2+
//     `<div style="margin-left:50%; margin-top:5%">
//     <button>
//         Edit
//     </button>
//     <button class="add_doctor_menu_btn_remove" value=${value.id}>
//         Remove
//     </button>
  
//     </div>` +
//     `</div> </br> </br>
//   `;

var content =
`<div class="flip-card">
<div class="flip-card-inner">
  <div class="flip-card-front">
    <img src="../images/image4.png" alt="Avatar" style="width:120px;height:120px;">
    <div class="name">${value.name}</div>
  </div>
  <div class="flip-card-back">
    <h1>Doctor</h1>
  </div>
</div>
</div>`
  
    return content;
};

$(document).ready(function () {
    $.ajax({
      url: "http://localhost:8050/speciality/display",
      type: "GET",
      success: function (result) {
        console.log(result);
        docList = result;
        console.log(docList);
        $.each(result, function (key, value) {
          console.log(value);
          // $(".add_doctor_personal").append("Name= " + value.name + "Speciality= " + value.speciality.name);
          $(".speciality_menu").append(constructSpecialityMenu(value));
        });
      },
      error: function (error) {
        console.log(error);
      },
    });
  });