$(document).ready(function() {
    
    $('#forgotPasswordForm').on('submit', function(e) {
        e.preventDefault(); // Prevent default form submission
        var email = $('#email').val(); // Get email value
    
    
        // Send Ajax request
        $.ajax({
          url: `http://localhost:8050/auth/forgotPassword?email=${email}`,
          method: 'GET',
          success: function(response) {
            // Handle successful response
            console.log(response);
            alert('Email Sent Successfully')
          },
          error: function(xhr, status, error) {
            // Handle error response
            console.log(xhr.responseText);
          }
        });
    });
  });