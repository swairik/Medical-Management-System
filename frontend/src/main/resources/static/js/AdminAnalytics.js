$(document).ready(function () {
    const cookie = document.cookie;
    if(cookie=='') window.location.href = "Auth";
    
    const token = cookie
      .split("; ")
      .find((row) => row.startsWith("authToken="))
      .split("=")[1];
    console.log(token);


  
    $.ajax({
      url: "http://localhost:8050/analytics/display/appointment/doctor",
      type: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        $("#Appointments_today").html(result.Appointments);
        $("#Doctors_today").html(result.Doctors);
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

    // $.ajax({
    //   url: "http://localhost:8050/analytics/display/appointment/doctor",
    //   type: "GET",
    //   headers: {
    //     Authorization: `Bearer ${token}`,
    //   },
    //   success: function (result) {
    //     console.log(result);
    //     $("#weekly_Appointments").html(result.Appointments);
    //     $("#weekly_Doctors").html(result.Doctors);
    //   },
    //   error: function (xhr, status, errorThrown) {
    //     if (xhr.status == 403) {
    //       window.location.href = "Auth";
    //     } else {
    //       var errorObj;
    //       if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
  
    //       if (errorObj) alert(errorObj.errorMessage);
    //       else alert("Some Error Occurred");
    //     }
    //   },
    // });

    $.ajax({
        url: "http://localhost:8050/analytics/display/specialityDoctorCount",
        type: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function (result) {
          console.log(result);
          const speciality = [];
          const doctor_count = [];
      
        Object.entries(result).forEach(([key, value]) => {
            
              console.log(`${key}: ${value}`);
              speciality.push(key);
              doctor_count.push(value)
          });
          var data = {
            labels: speciality,
            datasets: [{
              label: "Doctor Count By Speciality",
              backgroundColor: "rgb(234, 172, 168,0.8)",
              borderColor: "rgba(255,99,132,1)",
              borderWidth: 1,
              hoverBackgroundColor: "rgb(234, 172, 168,0.5)",
              hoverBorderColor: "rgba(255,99,132,1)",
              data: doctor_count,
            }]
          };
          var ctx = document.getElementById("doctor_speciality").getContext("2d");
            var myBarChart = new Chart(ctx, {
                type: 'bar',
                data: data
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

      $.ajax({
        url: "http://localhost:8050/analytics/display/patients/speciality",
        type: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        success: function (result) {
          console.log(result);
          const speciality = [];
          const patient_count = [];
      
        Object.entries(result).forEach(([key, value]) => {
            
              console.log(`${key}: ${value}`);
              speciality.push(key);
              patient_count.push(value)
          });
          var data = {
            labels: speciality,
            datasets: [{
              label: "Patient Count By Speciality",
              backgroundColor: "rgb(234, 172, 168,0.8)",
              borderColor: "rgba(255,99,132,1)",
              borderWidth: 1,
              hoverBackgroundColor: "rgb(234, 172, 168,0.5)",
              hoverBorderColor: "rgba(255,99,132,1)",
              data: patient_count,
            }]
          };
          var ctx = document.getElementById("patient_speciality").getContext("2d");
            var myBarChart = new Chart(ctx, {
                type: 'bar',
                data: data
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


  
  
  });