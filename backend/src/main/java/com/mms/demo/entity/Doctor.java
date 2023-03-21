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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
@Entity
@ToString
@Table(name = "DOCTOR", schema = "MMSYSTEM")
public class Doctor {
    @Id
    @Column(name = "doctor_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "doctor_name", nullable = false)
    private String name;

    @Column(name = "doctor_gender")
    @Pattern(regexp = "M|F")
    private String gender;

    @Column(name = "doctor_age", nullable = false)
    @Min(0)
    private Integer age;

    @Column(name = "doctor_email", length = 384, unique = true)
    @Email
    private String email;

    @Column(name="doctor_phone", length = 14, nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.DETACH})
    @JoinColumn(nullable = false, referencedColumnName = "speciality_id")
    private Speciality speciality;

    @Column(name="doctor_registration_timestamp", nullable = false)
    @Builder.Default
    private LocalDateTime stamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
}
