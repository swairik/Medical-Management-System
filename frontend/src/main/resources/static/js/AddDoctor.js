var req_id;

function add_value(data) {
  req_id = data;
}

$(document).ready(function () {
  const cookie = document.cookie;
  if (cookie == "") window.location.href = "Auth";

  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];

  console.log(token);

  $("#dfspeciality").click(function () {
    console.log("hi");
    if ($("#dfspeciality").val() === "add_speciality") {
      console.log("clicked");
      $(this).replaceWith($("#otherInput").show());
      // $("#otherInput").show()
    } else {
      $("#otherInput").hide();
    }
  });

  // $('#add_speciality').click(function() {

  //   console.log("clicked")
  //   $("#otherInput").show()
  //   // // get the value of the new option from the input field
  //   // var newOptionValue = newOptionInput.val();

  //   // // create a new option element and add it to the select
  //   // var newOption = $('<option>', {
  //   //   value: newOptionValue,
  //   //   text: newOptionValue
  //   // });
  //   // selectElement.append(newOption);

  //   // // clear the input field
  //   // newOptionInput.val('');
  // });

  $.ajax({
    url: "http://localhost:8050/speciality/display",
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
        $("#dfspeciality").prepend(
          `<option value=${value.id} selected>${value.name}</option>`
        );
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

  $(".dedit-form").submit(function (e) {
    e.preventDefault();

    if ($("#dfspeciality").val()) {
      var docData = {
        name: $("#dfname").val(),
        gender: $("#dfgender").val(),
        age: $("#dfage").val(),
        email: $("#dfemail").val(),
        phone: $("#dfcontact").val(),
        specialityId: $("#dfspeciality").val(),
      };

      $.ajax({
        url: "http://localhost:8050/doctor/",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(docData),
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function (result) {
          window.location.href = "EditDoctor";
          console.log(result);
        },
        error: function (xhr, status, errorThrown) {
          if (xhr.status == 403) {
            // window.location.href = "Auth";
          } else {
            var errorObj;
            if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

            if (errorObj) alert(errorObj.errorMessage);
            else alert("Some Error Occurred");
          }
        },
      });
    } else {
      var speciality = {
        name: $("#otherInput").val(),
      };

      var sid=null;
      
      $.ajax({
        url: "http://localhost:8050/speciality/",
        type: "POST",
        dataType: "json",
        async: false,
        contentType: "application/json",
        data: JSON.stringify(speciality),
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function (result) {
          console.log(result);
          sid=result.id
        },
        error: function (xhr, status, errorThrown) {
          if (xhr.status == 403) {
            // window.location.href = "Auth";
          } else {
            var errorObj;
            if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

            if (errorObj) alert(errorObj.errorMessage);
            else alert("Some Error Occurred");
          }
        },
      });

      var docData = {
        name: $("#dfname").val(),
        gender: $("#dfgender").val(),
        age: $("#dfage").val(),
        email: $("#dfemail").val(),
        phone: $("#dfcontact").val(),
        specialityId: sid,
      };

      $.ajax({
        url: "http://localhost:8050/doctor/",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(docData),
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function (result) {
          window.location.href = "EditDoctor";
          console.log(result);
        },
        error: function (xhr, status, errorThrown) {
          if (xhr.status == 403) {
            // window.location.href = "Auth";
          } else {
            var errorObj;
            if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);

            if (errorObj) alert(errorObj.errorMessage);
            else alert("Some Error Occurred");
          }
        },
      });
    }
  });
});
