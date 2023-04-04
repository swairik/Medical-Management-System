function formatDate(date) {
    var d = new Date(date),
      month = "" + (d.getMonth() + 1),
      day = "" + d.getDate(),
      year = d.getFullYear();
  
    if (month.length < 2) month = "0" + month;
    if (day.length < 2) day = "0" + day;
  
    return [year, month, day].join("/");
  }
  
  $(document).ready(function () {
    var curDateTime = new Date();
    var today = formatDate(curDateTime);
  
    const cookie = document.cookie;
  console.log(cookie)
  if(cookie=='') window.location.href = "Auth";

  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
    const doctor_id = cookie
    .split("; ")
    .find((row) => row.startsWith("id="))
    .split("=")[1];
  console.log(token)
  console.log(doctor_id);

  
    $.ajax({
      url: `http://localhost:8050/analytics/display/doctor/${doctor_id}`,
      type: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        console.log(result);
        // $("#Appointments_today").html(result.appointmentsToday);
        // $("#Doctors_today").html(result.activeDoctorsToday);
        // $("#Monthly_Appointments").html(result.appointmentsThisMonth);
        // $("#Monthly_Doctors").html(result.activeDoctorsThisMonth);
        // $("#Weekly_Appointments").html(result.appointmentsThisWeek);
        // $("#Weekly_Doctors").html(result.activeDoctorsThisWeek);
  
        // const speciality = [];
        // const doctor_count = [];
  
        // Object.entries(result.specialityDoctorCount).forEach(([key, value]) => {
        //   console.log(`${key}: ${value}`);
        //   const name = key.split('name=')[1].split(')')[0];
        //   console.log(name)
        //   speciality.push(name);
        //   doctor_count.push(value);
        // });
  
    
        // var data = {
        //   labels: speciality,
        //   datasets: [
        //     {
        //       label: "Doctor Count By Speciality",
        //       backgroundColor: "rgb(234, 172, 168,0.8)",
        //       borderColor: "rgba(255,99,132,1)",
        //       borderWidth: 1,
        //       hoverBackgroundColor: "rgb(234, 172, 168,0.5)",
        //       hoverBorderColor: "rgba(255,99,132,1)",
        //       data: doctor_count,
        //     },
        //   ],
        // };
        // var ctx = document.getElementById("doctor_speciality").getContext("2d");
        // var myBarChart = new Chart(ctx, {
        //   type: "bar",
        //   data: data,
        // });
  
        // const specialityPatient = [];
        // const patient_count = [];
  
        // Object.entries(result.specialityPatientCount).forEach(([key, value]) => {
        //   console.log(`${key}: ${value}`);
        //   const name = key.split('name=')[1].split(')')[0];
        //   console.log(name)
        //   specialityPatient.push(name);
        //   patient_count.push(value);
        // });
  
        // var data = {
        //   labels: specialityPatient,
        //   datasets: [
        //     {
        //       label: "Patient Count By Speciality",
        //       backgroundColor: "rgb(234, 172, 168,0.8)",
        //       borderColor: "rgba(255,99,132,1)",
        //       borderWidth: 1,
        //       hoverBackgroundColor: "rgb(234, 172, 168,0.5)",
        //       hoverBorderColor: "rgba(255,99,132,1)",
        //       data: patient_count,
        //     },
        //   ],
        // };
        // var ctx1 = document.getElementById("patient_speciality").getContext("2d");
        // var myBarChart = new Chart(ctx1, {
        //   type: "bar",
        //   data: data,
        // });
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
  