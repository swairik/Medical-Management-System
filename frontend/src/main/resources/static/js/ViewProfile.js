$(document).ready(function () {
    
        $(".dedit").hide();
        $("#edit").click(function(){
            $(".dedit").show();
           $(".card").hide();
    
        });
        $("#edited").click(function(){
            $(".card").show();
           $(".dedit").hide();
    
        })

        const cookie = document.cookie;
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  const doctor_id = cookie
    .split("; ")
    .find((row) => row.startsWith("id="))
    .split("=")[1];
  console.log(token);
  console.log(doctor_id);
          
    
    $.ajax({
        url: `http://localhost:8050/doctor/display/${doctor_id}`,
        type: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function (result) {
          console.log(result);
          $("#dname").html(result.name);
        $("#dage").html(result.age);
        $("#dgender").html(result.gender);
        $("#dspeciality").html(result.speciality.name);
        $("#dcontact").html(result.phone);
        $("#demail").html(result.email);

        $("#dfname").val(result.name);
        $("#dfage").val(result.age);
        $("#dfgender").val(result.gender);
        $("#dfspeciality").val(result.speciality.name);
        $("#dfspeciality").data('value',result.speciality.id)
        $("#dfcontact").val(result.phone);
        $("#dfemail").val(result.email);
          
        },
        error: function (xhr, status, error) {
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

      $('#doctor-form').on('submit', function(e) {
        e.preventDefault(); // Prevent default form submission
        
        var docData= {
            "name":$("#dfname").val(),
        "age":$("#dfage").val(),
        "gender":$("#dfgender").val(),
        "specialityId":$("#dfspeciality").data().value,
        "phone":$("#dfcontact").val(),
        "email":$("#dfemail").val()
        }

        console.log(docData)
    
    
        // Send Ajax request
        $.ajax({
          url: `http://localhost:8050/doctor/${doctor_id}`,
          method: 'PUT',
          dataType: 'json',
        contentType: 'application/json',
          data: JSON.stringify(docData),
          headers: {
            Authorization: `Bearer ${token}`,
          },
          success: function(response) {
            // Handle successful response
            console.log(response);
            alert('Updated Successfully!')
            window.location.href = 'ViewProfile';
          },
          error: function(xhr, status, error) {
            // Handle error response
            var errorObj;
            if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
    
            if (errorObj) alert(errorObj.errorMessage);
            else alert("Some Error Occurred");
          }
        });
    });

})