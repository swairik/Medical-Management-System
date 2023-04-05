const constructSpecialityMenu = (value) => {
  var content = `
  <div class="card">
    <div class="card-inner">
    <img src="../images/3763028.jpg" alt="Avatar" style="width:250px;height:250px;">
        <div class="name"><a href="#docList" rel="modal:open" value=${value.id} id="show_doctor">${value.name}</a></div>
    </div>
  </div>
 `;

  return content;
};

$(document).ready(function () {
  const cookie = document.cookie;
  console.log(cookie);

  if (cookie == "") window.location.href = "Auth";

  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  console.log(token);

  var doctor_array = [];

  $(document).click(function () {
    // code to handle the click event
    $("#doctor_search_result").empty();
    $("#doctor_search").val("");
  });

  $.ajax({
    url: "http://localhost:8050/doctor/display",
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
        doctor_array.push(value);
        // console.log(doctor_array)
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

  console.log(doctor_array);

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
        $(".grid").append(constructSpecialityMenu(value));
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

  $(".grid").on("click", "#show_doctor", function (e) {
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
          console.log(value.ratingAverage)
          $("#doctor_list").append(
            `<ul>
            <li><button id="show_slot" value=${value.id}>${
              value.name
            }</button></li>
           
            <div class="rating">
            <input type="radio" name="star" value="5" ${
              value.ratingAverage === 5 ? 'checked' : ''
            }><span class="star"> </span>
            <input type="radio" name="star" value="4" checked=${
              value.ratingAverage === 4 ? 'checked' : ''
            } disabled><span class="star"> </span>
            <input type="radio" name="star" value="3" ${
              value.ratingAverage === 3
            } disabled><span class="star"> </span>
            <input type="radio" name="star" value="2" checked=${
              value.ratingAverage === 2
            } disabled><span class="star"> </span>
            <input type="radio" name="star" value="1"  disabled><span class="star"> </span>
          </div>
          
            </ul>
            `
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

  $("#doctor_search").on("keyup", function () {
    // Get the current value of the input field
    var filterValue = $(this).val().toLowerCase();

    console.log(filterValue);

    var filteredData = doctor_array.filter(function (item) {
      return item.name.toLowerCase().indexOf(filterValue) > -1;
    });

    console.log(filteredData);

    $("#doctor_search_result").empty();

    $.each(filteredData, function (index, item) {
      $("#doctor_search_result").append(
        `<li value=${item.id}>` + item.name + "</li>"
      );
    });
  });

  $("#doctor_search_result").on("click", "li", function () {
    window.location.href = "BookAppointment?id=" + this.value;
  });
});
