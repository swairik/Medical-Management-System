package com.mms.demo.service;

import com.mms.demo.entity.Patient;
import com.mms.demo.transferobject.AppointmentDetailsDTO;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.AppointmentDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDetailsService {

    Optional<AppointmentDetailsDTO> get(Long id);


    Optional<AppointmentDetailsDTO> update(Long id, AppointmentDetailsDTO updates);
}
