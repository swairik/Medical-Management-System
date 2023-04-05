$(document).ready(function () {
  const cookie = document.cookie;
  if (cookie == "") window.location.href = "Auth";

  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  const patient_id = cookie
    .split("; ")
    .find((row) => row.startsWith("id="))
    .split("=")[1];
  console.log(token);
  console.log(patient_id);

  console.log(
    new Date()
      .toLocaleString("en-US", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false,
      })
      .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6")
  );

  $.ajax({
    url: `http://localhost:8050/appointment/display/patient/${patient_id}/unFilledFeedback/7`,
    type: "GET",
    data: {
      stamp: new Date()
        .toLocaleString("en-US", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
          hour12: false,
        })
        .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6"),
    },
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result.length);
      // var oldValue = $('#span_count').attr('data-count');
     
      
      // $('#span_count').data('count', '15');
     

      
      $.each(result, function (key, value) {
        console.log(value);
        $("#patient_feedback").append(
          `<li><a class="link" href="PatientFeedback?appointmentDetailsId=${
            value.appointmentDetails.id
          }&appointmentId=${
            value.id
          }">Please Provide<br>your valuable feedback dated ${value.start.substring(
            0,
            value.start.indexOf("T")
          )}<br></a></li>`
        );
      });
    },
    error: function (xhr, status, errorThrown) {
      if (xhr.status == 403) {
        window.location.href = "Auth";
      } else {
        alert("Some Error Occurred");
      }
    },
  });
});
