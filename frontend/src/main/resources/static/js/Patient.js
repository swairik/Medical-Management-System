const constructSpecialityMenu = (value) => {
var content =
`<div class="flip-card">
<div class="flip-card-inner">
  <div class="flip-card-front">
    <img src="../images/image4.png" alt="Avatar" style="width:120px;height:120px;">
    <div class="name">${value.name}</div>
  </div>
  <div class="flip-card-back">
    <h1>Doctor</h1>
    <p>Doc-1</p>
    <p>Doc-2</p>
  </div>
</div>
</div>`
  
    return content;
};

$(document).ready(function () {
    const cookie = document.cookie;
    const token = cookie.split('; ').find(row => row.startsWith('authToken=')).split('=')[1];
    console.log(token)
    var req=$.ajax({
      url: "http://localhost:8050/speciality/display",
      type: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      },
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

    console.log(req)


  });