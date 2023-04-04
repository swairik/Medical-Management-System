package com.mms.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.AppointmentDetails;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Speciality;

/**
 * AppointmentRepository defines an interface to generate JPA defined queries to interact with the
 * Appointment table.
 * 
 * @author Mananveer Singh
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    /**
     * Find all appointments that correspond to the same patient.
     * 
     * @see Patient
     * @param patient the patient
     * @return the list of appointments that have the given patient
     */
    List<Appointment> findAllByPatient(Patient patient);

    List<Appointment> findAllByPatientAndStartGreaterThanEqual(Patient patient,
                    LocalDateTime start);

    List<Appointment> findAllByPatientAndStartBetween(Patient patient, LocalDateTime start,
                    LocalDateTime end);

    List<Appointment> findAllByDoctor(Doctor doctor);

    List<Appointment> findAllByDoctorAndStartBetween(Doctor doctor, LocalDateTime start,
                    LocalDateTime end);

    List<Appointment> findAllByDoctorAndStartGreaterThanEqual(Doctor doctor, LocalDateTime start);

    List<Appointment> findAllByPatientAndDoctor(Patient patient, Doctor doctor);

    List<Appointment> findAllByPatientAndDoctorAndStartBetween(Patient patient, Doctor doctor,
                    LocalDateTime start, LocalDateTime end);

    List<Appointment> findAllByPatientAndDoctorAndStartGreaterThanEqual(Patient patient,
                    Doctor doctor, LocalDateTime start);

    List<Appointment> findAllByStartBetween(LocalDateTime start, LocalDateTime end);

    Optional<Appointment> findByAppointmentDetails(AppointmentDetails appointmentDetails);

    Long countByDoctor_Speciality(Speciality speciality);


    Long countByDoctorAndAppointmentDetails_PrescriptionNull(Doctor doctor);

}
