const constructScheduleMenu = (value) => {
    var div=`
    <div class="box_grp">
    <div class="box_one">
    <input type="text" class="text" value=${value.doctorResponse.name}>
    <input type="text" class="text" value=${value.doctorResponse.speciality.name}>
    <input type="text" class="text" value=${value.doctorResponse.email}>
    <input type="text" class="text" value=${value.doctorResponse.phone}>
</div>

<div class="box_two">
            
<label class="text_two"> Year:
    <input type="text" class="text_two" value=${value.year} />
    </label>

    <label class="text_two"> Year:
    <input type="text" class="text_two" value=${value.week} />
    </label>
        
        <br/>
        <input type="text" class="text_two" value="Slot-Timing: " />
        <br/>
        <label class="text_two"> Start-Time:
        <input type="text" class="text_two" value=${value.slotResponse.start} />
        </label>
        <label class="text_two"> End-Time:
        <input type="text" class="text_two" value=${value.slotResponse.end} />
        </label>
        <label class="text_two"> Capacity:
        <input type="text" class="text_two" value=${value.slotResponse.capacity} />
        </label>
        <label class="text_two"> WeekDay:
        <input type="text" class="text_two" value=${value.slotResponse.weekday} />
        </label>
    

<div class="btn_grp">
    <button class="btn_accept">Accept</button>
    <button class="btn_remove">Remove</button>
</div>
</div>
    </div>`
  
    
  
    return div;
  };
  

$(document).ready(function () {
    $.ajax({
        url: "http://localhost:8050/schedule/display",
        type: "GET",
        success: function (result) {
          console.log(result);
          $.each(result, function (key, value) {
            console.log(value);
            // $(".add_doctor_personal").append("Name= " + value.name + "Speciality= " + value.speciality.name);
            $("#schedule_menu").append(constructScheduleMenu(value));
          });
        },
        error: function (error) {
          console.log(error);
        },
      });
  
  });