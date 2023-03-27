package com.mms.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;

/**
 * DoctorRepository defines an interface to generate JPA defined queries to interact with the Doctor
 * table.
 * 
 * @author Mananveer Singh
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    /**
     * Find all the doctors that specialize in the same speciality.
     *
     * @param speciality the speciality
     * @return the list of Doctors that share the given Speciality.
     */
    List<Doctor> findAllBySpeciality(Speciality speciality);

    /**
     * Find the doctor with the given email.
     *
     * @param email the email
     * @return the Doctor with the given email
     */
    Optional<Doctor> findByEmail(String email);

    /**
     * Find all doctors that were registered in a given time range. Meant for report keeping, so no
     * endpoints from the Controller layer map to this function.
     * 
     * @param start the start of the range
     * @param end the end of the range
     * @return the list of doctors that registered in the given range
     */
    List<Doctor> findAllByStampBetween(LocalDateTime start, LocalDateTime end);
}
