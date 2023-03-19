const constructSpecialityMenu = (value) => {
  var content = `<div class="grid">
<div class="card">
  <div class="card-inner">
    <img
      src="images/image4.png"
      alt="Avatar"
      style="width: 120px; height: 120px"
    />
    <div class="name">
      <a href="#doctorList" rel="modal:open" value=${value.id} id="show_doctor">${value.name}</a>
    </div>
  </div>
</div>
</div>`;

  return content;
};

$(document).ready(function () {
  const cookie = document.cookie;
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  console.log(token);

  $.ajax({
    url: "http://localhost:8050/speciality/display",
    type: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    success: function (result) {
      console.log(result);
      docList = result;
      console.log(docList);
      $.each(result, function (key, value) {
        console.log(value);
        $(".specialities").append(constructSpecialityMenu(value));
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

  $(".specialities").on("click", "#show_doctor", function (e) {
    console.log("clicked");

    e.preventDefault();
    $.ajax({
      type: "GET",
      url: `http://localhost:8050/doctor/display/speciality/${this.getAttribute(
        "value"
      )}`,
      headers: {
        Authorization: `Bearer ${token}`,
      },
      success: function (result) {
        $.each(result, function (key, value) {
          console.log(value);
          $("#doctor_list").append(
            `<li><button id="show_slot" value=${value.id}>${value.name}</button></li>`
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

  $("#doctor_list").on("click", "button#show_slot", function (e) {
    console.log("clicked");
    console.log(this.value);

    window.location.href = "BookAppointment?id=" + this.value;
  });
});
