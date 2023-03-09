package com.mms.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "CREDENTIALS", schema = "MMSYSTEM") 
public class Credential {
    @Id
    @Column(name = "credential_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="credential_email", nullable = false, unique = true)
    private String email;

    @Column(name="credential_password", nullable = false)
    private String password;

    @Column(name="credential_is_patient", nullable = false)
    private Boolean isPatient;

    @Column(name="credential_is_admin", nullable = false)
    private Boolean isAdmin;

}
