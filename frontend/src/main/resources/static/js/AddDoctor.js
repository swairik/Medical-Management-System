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
          $("#speciality_menu").append(`<option value=${value.id}>${value.name}</option>`);
        });
      },
      error: function (error) {
        console.log(error);
      },
    });

    
  
    $("#add_doctor").click(function(e) {
      console.log("clicked")
      var docData = {
        "name": $('#name').val(),
        "gender": "M",
        "age": $('#age').val(),
        "email": "email41@gmail.com",
        "phone":  "123417219",
        "specialityId": 1
      }

      // $('#name').val()
      console.log(docData)
      e.preventDefault();
      $.ajax({
        url: "http://localhost:8050/doctor/",
        type: "POST",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(docData),
        success: function (result) {
          window.location.href = 'EditDoctor';
          console.log(result);
        },
        error: function (error) {
          console.log(error);
        },
      });
     
    });
    
  });