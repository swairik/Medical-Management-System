package com.mms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mms.demo.entity.AppointmentDetails;

public interface AppointmentDetailsRepository extends JpaRepository<AppointmentDetails, Long> {

}
