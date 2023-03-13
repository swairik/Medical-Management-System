function getWeekNumber(d) {
    // Copy date so don't modify original
    d = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
    // Set to nearest Thursday: current date + 4 - current day number
    // Make Sunday's day number 7
    d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay()||7));
    // Get first day of year
    var yearStart = new Date(Date.UTC(d.getUTCFullYear(),0,1));
    // Calculate full weeks to nearest Thursday
    var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7);
    // Return array of year and week number
    return [d.getUTCFullYear(), weekNo];
}

function getDate(d) {
    var date = d.toJSON().slice(0, 10);
    var nDate = date.slice(0, 4) + '-' + date.slice(5, 7) + '-' + date.slice(8, 10);
    return nDate;
}

var curDateTime=new Date()
console.log(curDateTime.getDay())

//console.log(curDateTime.getHours() + ":" + curDateTime.getMinutes() + ":" + curDateTime.getSeconds())

var result = getWeekNumber(curDateTime);
console.log(result)

console.log(getDate(curDateTime))

// .min = "2023-W16";
// `${result[0]}-W${result[1]}`

$(document).ready(function () {

    var curDT;
    var week_id =  document.getElementById("week")
    var start_time_id = document.getElementById("start_time")
    var end_time_id = document.getElementById("end_time")
    var date_id = document.getElementById("date")
    
    week_id.min = `${result[0]}-W${result[1]}`
    week_id.value= `${result[0]}-W${result[1]}`

    start_time_id.value = (curDateTime.getHours()<10?'0':'') + curDateTime.getHours() + ":" + (curDateTime.getMinutes()<10?'0':'') + curDateTime.getMinutes()
    end_time_id.value = (curDateTime.getHours()<10?'0':'') + curDateTime.getHours() + ":" + (curDateTime.getMinutes()<10?'0':'') + curDateTime.getMinutes()
    date_id.value=getDate(curDateTime)
    
    curDT=new Date(date.value)


    var slotID=12;

    $.ajax({
        url: "http://localhost:8050/schedule/display/doctor/2",
        type: "GET",
        success: function (result) {
          console.log(result);
         
          $.each(result, function (key, value) {
            console.log(value);
            // $(".add_doctor_personal").append("Name= " + value.name + "Speciality= " + value.speciality.name);
            $("#schedule_table").append(`<tr>
                        <td>${value.weekDate}</td>
                        <td>${value.weekDate}</td>
                        <td>${value.slotResponse.weekday}</td>
                        <td>${value.slotResponse.start}</td>
                        <td>${value.slotResponse.end}</td>
                        <td>${value.slotResponse.capacity}</td>
                        <td>${value.approval?"Approved":"Not Approved"}</td>
                        <td><button id="remove_schedule" value=${value.id}>Remove</button></td>
                      </tr>`);
          });
        },
        error: function (error) {
          console.log(error);
        },
      });


    $("#save_button").click(function(e) {
        
        var slotData = {
            "weekday": 2,
            "start": $('#start_time').val(),
            "end": $('#end_time').val()
        }

        console.log(slotData)


        console.log("clicked")
        e.preventDefault();
        $.ajax({
            url: "http://localhost:8050/slot/",
            type: "POST",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(slotData),
            success: function (result) {
                console.log(result);
                
                var scheduleData = {
                    "doctorId": 2,
                    "slotId": result.id,
                    "weekDate": $('#week').val()
                }
                console.log(scheduleData)
                $.ajax({
                    url: "http://localhost:8050/schedule/",
                    type: "POST",
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(scheduleData),
                    success: function (result) {
                        console.log(result);
                      window.location.href = 'UpdateSchedule';
                    },
                    error: function (error) {
                      console.log(error);
                    },
                });
            },
            error: function (error) {
              console.log(error);
            },
        });
      });

      $("#schedule_table").on("click","button#remove_schedule",function(e) {
        console.log("clicked")
        console.log(this)
        e.preventDefault();
        $.ajax({
          type: "DELETE",
          url: `http://localhost:8050/schedule/${this.value}`,
          success: function(result) {
            window.location.href = 'UpdateSchedule';
          },
          error: function(result) {
            alert('error');
          }
        });
      });

      
})