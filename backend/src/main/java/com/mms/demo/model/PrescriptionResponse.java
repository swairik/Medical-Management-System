package com.mms.demo.model;

import java.time.LocalDateTime;
import java.util.Base64;

import com.mms.demo.controller.AppointmentController;
import com.mms.demo.entity.Prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {
    private Long id;
    private DoctorResponse doctorResponse;
    private AppointmentResponse appointmentResponse;
    private LocalDateTime stamp;
    private PrescriptionContentResponse contents;

    public static PrescriptionResponse createResponseFromPrescription(Prescription prescription,
            AppointmentResponse aptResponse) {
        String delimiter = ":,-";
        String[] contentsResponse = new String(Base64.getDecoder().decode(prescription.getContents())).split(delimiter);
        PrescriptionResponse prescriptionResponse = PrescriptionResponse.builder()
                .id(prescription.getId())
                .doctorResponse(DoctorResponse.createResponseFromDoctor(prescription.getDoctor()))
                .appointmentResponse(aptResponse)
                .stamp(prescription.getStamp())
                .contents(PrescriptionContentResponse.createResponseFromPrescriptionContent(contentsResponse))
                .build();
        return prescriptionResponse;
    }

}
