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
import jakarta.persistence.Lob;
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
@Table(name = "APPOINTMENT_DETAILS", schema = "MMSYSTEM")
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppointmentDetails {
    /**
     * The Primary Key. Autogenerated by the DBMS on the creation of an entry.
     */
    @Id
    @Column(name = "appointment_details_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "prescription_id", nullable = false)
    private Prescription prescription;

    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "feedback_id", nullable = false)
    private Feedback feedback;

    @Column(name = "appointment_details_stamp")
    private LocalDateTime stamp;
}
