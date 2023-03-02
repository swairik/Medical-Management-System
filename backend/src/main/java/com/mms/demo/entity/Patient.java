package com.mms.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "PATIENT", schema = "MMSYSTEM")
public class Patient {
    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "patient_name", nullable = false)
    private String name;

    @Column(name = "patient_gender")
    @Pattern(regexp = "M|F")
    private String gender;

    @Column(name = "patient_age", nullable = false)
    @Min(0)
    private Integer age;

    @Column(name = "patient_email", length = 384)
    @Email
    private String email;

    @Column(name="patient_phone", length = 14, nullable = false)
    private String phone;

}
