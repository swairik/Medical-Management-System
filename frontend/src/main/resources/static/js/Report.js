$(document).ready(function () {
  const cookie = document.cookie;
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  console.log(token);

  //   console.log($("#start_date_report").val())

  //   const start = new Date($("#start_date_report").val());

  //   console.log(start);

  //   const formattedStartDate = start
  //     .toLocaleString("en-US", {
  //       year: "numeric",
  //       month: "2-digit",
  //       day: "2-digit",
  //       hour: "2-digit",
  //       minute: "2-digit",
  //       second: "2-digit",
  //       hour12: false,
  //     })
  //     .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6");

  //   console.log(formattedStartDate);

  //   const end = new Date($("#end_date_start").val());

  //   console.log(end);

  //   const formattedEndDate = end
  //     .toLocaleString("en-US", {
  //       year: "numeric",
  //       month: "2-digit",
  //       day: "2-digit",
  //       hour: "2-digit",
  //       minute: "2-digit",
  //       second: "2-digit",
  //       hour12: false,
  //     })
  //     .replace(/(\d+)\/(\d+)\/(\d+), (\d+):(\d+):(\d+)/, "$3/$1/$2 $4:$5:$6");
  //   console.log(formattedEndDate);

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
      url: `http://localhost:8050/report/display/generateReport?from=${formattedStartDate}&to=${formattedEndDate}`,
      type: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      responseType: "arraybuffer",
      success: function (result) {
        alert("Downloading..")
        
        // Create a new Blob object from the response data
        var blob = new Blob([result], { type: "application/octet-stream" });
        var url = URL.createObjectURL(blob);
        
        // Create a new hidden a tag with the download URL
        var a = document.createElement("a");
        a.href = url;
        a.download = "Report.zip";
        a.style.display = "none";
        
        // Append the a tag to the body and simulate a click on it to start the download
        document.body.appendChild(a);
        a.click();
        
        // Remove the a tag from the body after the download is complete
        document.body.removeChild(a);
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
