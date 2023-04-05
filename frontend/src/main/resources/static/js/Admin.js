$(document).ready( function () {
  $.getScript("https://cdn.datatables.net/1.10.25/js/jquery.dataTables.min.js", function() {
  const cookie = document.cookie;

  if(cookie=='') window.location.href = "Auth";
  const token = cookie
    .split("; ")
    .find((row) => row.startsWith("authToken="))
    .split("=")[1];
  console.log(token);

  
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
      
      
        $('#example').DataTable(
          {
            data: docList,
            columns: [
                { data: 'name' },
                { data: 'age' },
                { data: 'gender' },
                { data: 'phone' },
                { data: 'email' },
                { data: 'speciality.name' },
                { data: 'id',
                  render: function(data, type, row) {
                  return '<button type="button" class="add_doctor_menu_btn_edit" value='+data+'>Edit</button>';
                }},
                { data: 'id',
                  render: function(data, type, row) {
                  return '<button type="button" class="add_doctor_menu_btn_remove" value='+data+'>Remove</button>';
                }}
            ],
            ordering: false,
        });
        $('#dis').show();

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

  $("#example").on("click","button.add_doctor_menu_btn_edit",function(e) {
    console.log("clicked")
    console.log(this)
    e.preventDefault();
    window.location.href = 'DoctorEditProfile?doctor_id=' + this.value;
    // $.ajax({
    //   type: "DELETE",
    //   url: `http://localhost:8050/doctor/${this.value}`,
    //   headers: {
    //     Authorization: `Bearer ${token}`,
    //   },
    //   success: function(result) {
    //     alert('Doctor Removed Successfully...')
    //     window.location.href = 'EditDoctor';
    //   },
    //   error: function(xhr, status, errorThrown) {
    //     if (xhr.status == 403) {
    //       window.location.href = "Auth";
    //     } else {
    //       var errorObj;
    //       if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
  
    //       if (errorObj) alert(errorObj.errorMessage);
    //       else alert("Some Error Occurred");
    //     }
    //   }
    // });
  });

  $("#example").on("click","button.add_doctor_menu_btn_remove",function(e) {
    console.log("clicked")
    console.log(this)
    e.preventDefault();
    // $.ajax({
    //   type: "DELETE",
    //   url: `http://localhost:8050/doctor/${this.value}`,
    //   headers: {
    //     Authorization: `Bearer ${token}`,
    //   },
    //   success: function(result) {
    //     alert('Doctor Removed Successfully...')
    //     window.location.href = 'EditDoctor';
    //   },
    //   error: function(xhr, status, errorThrown) {
    //     if (xhr.status == 403) {
    //       window.location.href = "Auth";
    //     } else {
    //       var errorObj;
    //       if (xhr.responseText) errorObj = JSON.parse(xhr.responseText);
  
    //       if (errorObj) alert(errorObj.errorMessage);
    //       else alert("Some Error Occurred");
    //     }
    //   }
    // });
  });

});
});


