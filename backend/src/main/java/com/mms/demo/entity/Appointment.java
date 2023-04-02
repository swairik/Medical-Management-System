package com.mms.demo.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Appointment is an entity that represents a table specifically meant to map Patient entities to
 * Slot entities. It encapsulates the information needed to clarify which Patient gets to visit a
 * Doctor at which Slot. Patients can have multiple Slots assigned to them, as well the same Slot
 * being assigned to multiple Patients, the latter being dictated by the Slot's capacity
 * {@see Slot}. Can be identified by a surrogate ID.
 * <p>
 * Supports a Builder() as well as a toBuilder(), which are the preferred methods of constructing
 * objects of this class.
 * 
 * @author Mananveer Singh
 */
@Entity
@Table(name = "APPOINTMENT", schema = "MMSYSTEM")
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Appointment {

    /**
     * Primary Key for this entity. While another candidate key exists, identifying using a
     * surrogate key when you already have the object is far faster and easier to work with.
     * Intended to be generated automatically by the Database Management System.
     */
    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * A reference to the Patient table. Defines the ownership of an entry from the side of a
     * Patient. A Patient can have multiple appointments in differen Slots.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "doctor_id")
    private Doctor doctor;

    @Column(name = "appointment_start", nullable = false)
    private LocalDateTime start;

    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private AppointmentDetails appointmentDetails;

    /**
     * A boolean flag that represents whether the appointment was attended by the patient or not.
     */
    @Column(name = "appointment_attended", nullable = false)
    @Builder.Default
    private Boolean attended = false;

    /**
     * The timestamp of the creation of the appointment.
     */
    @Column(name = "appointment_scheduled_on", nullable = false)
    @Builder.Default
    private LocalDateTime stamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
}
