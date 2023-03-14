package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.mms.demo.entity.Appointment;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {
    private Long id;
    private PatientResponse patientResponse;
    private SlotResponse slotResponse;

    public static AppointmentResponse createResponseFromAppointment(Appointment appointment) {
        PatientResponse patientResponse = PatientResponse.createResponseFromPatient(appointment.getPatient());
        SlotResponse slotResponse = SlotResponse.createResponseFromSlot(appointment.getSlot());
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(appointment.getId())
                .patientResponse(patientResponse)
                .slotResponse(slotResponse)
                .build();
        return appointmentResponse;
    }

}
