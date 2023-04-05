$(document).ready(function() {
    var url = window.location.href;
    
    var email = url.substring(url.lastIndexOf('=') + 1);
    var token = url.substring(url.indexOf('=') + 1, url.lastIndexOf('&'));

    console.log(email)
    console.log(token)

    $('form').on('submit', function(e) {
        e.preventDefault(); // Prevent default form submission
        
        var updatePasswordData = {
            "email": email,
            "password": $('#password').val(),
            "confirmPassword": $('#confirm-password').val(),
            "token": token
        }

        console.log(updatePasswordData)
        // Send Ajax request
        $.ajax({
          url: "http://localhost:8050/auth/updatePassword",
          method: 'POST',
          dataType: 'json',
          contentType: 'application/json',
          data: JSON.stringify(updatePasswordData),
          success: function(response) {
            // Handle successful response
            console.log(response);
            // alert('Password Updated Successfully')
            swal("Password Updated Successfully!", "", "success", {
              button: "OK",
            });
          },
          error: function(xhr, status, error) {
            // Handle error response
            console.log(xhr.responseText);
        
            if((JSON.parse(xhr.responseText)).errorCode) alert((JSON.parse(xhr.responseText)).errorCode)
            // else alert('Some Error Occurred!')
            swal("", "Some Error Occurred", "error", {
              button: "OK",
            });
          }
        });
    });
    
    // $.ajax({
    //   type: "POST",
    //   url: "/updatePassword",
    //   data: {
    //     token: token,
    //     email: email
    //   },
    //   success: function(data) {
    //     console.log(data);
    //   },
    //   error: function(jqXHR, textStatus, errorThrown) {
    //     console.log(textStatus, errorThrown);
    //   }
    // });
  });