package com.mms.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionContentRequest {
    @Builder.Default
    String medication = "";
    @Builder.Default
    String test = "";
    @Builder.Default
    String diagnosis = "";
}
