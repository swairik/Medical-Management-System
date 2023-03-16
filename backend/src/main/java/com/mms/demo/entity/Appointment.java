package com.mms.demo.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "patient_id")
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "slot_id")
    private Slot slot;

    @Column(name = "appointment_attended", nullable = false)
    @Builder.Default
    private Boolean attended = false;

    @Column(name = "appointment_scheduled_on", nullable = false)
    @Builder.Default
    private LocalDateTime stamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
}
