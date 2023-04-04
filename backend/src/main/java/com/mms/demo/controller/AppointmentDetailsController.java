package com.mms.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.exception.CustomException;
import com.mms.demo.service.AppointmentDetailsService;
import com.mms.demo.transferobject.AppointmentDetailsDTO;

@CrossOrigin("*")
@RestController
@RequestMapping("/appointmentDetails")
public class AppointmentDetailsController {

    @Autowired
    AppointmentDetailsService appointmentDetailsService;

    @GetMapping("/display/{id}")
    public ResponseEntity<AppointmentDetailsDTO> showAppointmentDetailsById(@PathVariable Long id) {
        AppointmentDetailsDTO appointmentDetails = appointmentDetailsService.get(id)
                .orElseThrow(() -> new CustomException("Appointment Details with given id not found",
                        "APPOINTMENT_DETAILS_NOT_FOUND", HttpStatus.NOT_FOUND));

        return new ResponseEntity<>(appointmentDetails, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDetailsDTO> updateAppointmentDetailsById(@PathVariable Long id,
            @RequestBody AppointmentDetailsDTO appointmentDetails) {
        AppointmentDetailsDTO updatedAppointmentDetails = appointmentDetailsService.update(id, appointmentDetails)
                .orElseThrow(() -> new CustomException("Appointment Details not updated",
                        "APPOINTMENT_DETAILS_NOT_UPDATED", HttpStatus.INTERNAL_SERVER_ERROR));

        return new ResponseEntity<>(updatedAppointmentDetails, HttpStatus.OK);
    }

}
