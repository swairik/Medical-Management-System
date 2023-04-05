$(document).ready(function () {
      
    const cookie = document.cookie;
    if (cookie == "") window.location.href = "Auth";
  
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
        url: `http://localhost:8050/analytics/display/doctor/${doctor_id}`,
        type: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function (result) {
          console.log(result);
          $("#u_app").html(result.upcomingAppointments);
          $("#ptotal").html(result.patientCount);
          $("#no_pres").html(result.unfilledPrescriptions);
          if(result.unfilledPrescriptions===0){
            $("#complete").show();
          }
          else{
            $("#incomplete").show();
          }
        },
        error: function (xhr, status, errorThrown) {
            if (xhr.status == 403) {
              window.location.href = "Auth";
            } else {
              var errorObj;
              if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
      
              // if (errorObj) alert(errorObj.errorMessage);
              // else alert("Some Error Occurred");
              if (errorObj) {
                swal(errorObj.errorMessage, "", "error", {
                  button: "OK",
                });
              }
              else 
              {
                swal("Some Error Occurred", "", "error", {
                  button: "OK",
                });
              }
            }
          },
        }); 
}); 