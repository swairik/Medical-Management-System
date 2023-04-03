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

  
    $("#download_report").click(function () {
      // Your code here

      console.log($("#start_date_report").val());
  
      const start = new Date($("#start_date_report").val());
  
      console.log(start);
  
      const formattedStartDate = start
        .toLocaleString("en-US", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
          hour12: false,
        })
        .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6");
  
      console.log(formattedStartDate);
  
      const end = new Date($("#end_date_report").val());
  
      console.log(end);
  
      const formattedEndDate = end
        .toLocaleString("en-US", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit",
          hour12: false,
        })
        .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6");
      console.log(formattedEndDate);
  
      $.ajax({
        url: `http://localhost:8050/report/display/generateDoctorReport/${doctor_id}?from=${formattedStartDate}&to=${formattedEndDate}`,
        type: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        xhrFields: {
          responseType: "blob",
        },
        success: function (data,status,xhr) {
          alert("Downloading..");
          console.log(data);
  
          // const filename = xhr.getResponseHeader('Content-Disposition')
          //             .match(/filename="?(.+)"?/)[1];
          // console.log(filename)
  
          const url = window.URL.createObjectURL(data);
          const a = document.createElement("a");
          a.href = url;
          a.download = "ReportDoctor.zip";
          document.body.appendChild(a);
          a.click();
          a.remove();
        },
        error: function (xhr, status, errorThrown) {
          if (xhr.status == 403) {
            window.location.href = "Auth";
          } else {
            var errorObj;
            if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
  
            if (errorObj) alert(errorObj.errorCode);
            else alert("Some Error Occurred");
          }
        },
      });
    });

  });
  