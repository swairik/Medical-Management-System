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
    
       
          
    
    $.ajax({
        url: "http://localhost:8050/doctor/display/1",
        type: "GET",
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
        // console.log($("#dfspeciality").data())
        $("#dfcontact").val(result.phone);
        $("#dfemail").val(result.email);
          
        },
        error: function (xhr, status, error) {
          console.log(error);
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
          url: `http://localhost:8050/doctor/1`,
          method: 'PUT',
          dataType: 'json',
        contentType: 'application/json',
          data: JSON.stringify(docData),
          success: function(response) {
            // Handle successful response
            console.log(response);
            alert('Updated Successfully!')
            window.location.href = 'ViewProfile';
          },
          error: function(xhr, status, error) {
            // Handle error response
            console.log(xhr.responseText);
            alert('Some Error Occurred!')
          }
        });
    });

})