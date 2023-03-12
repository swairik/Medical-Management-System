package com.mms.demo.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Credential implements UserDetails {
    @Id
    @Column(name = "credential_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "credential_email", nullable = false, unique = true)
    private String email;

    @Column(name = "credential_password", nullable = false)
    private String password;

    // @Column(name = "credential_is_patient", nullable = false)
    // private Boolean isPatient;

    // @Column(name = "credential_is_admin", nullable = false)
    // private Boolean isAdmin;

    @Enumerated(EnumType.STRING)
    @Column(name = "credential_role", nullable = false)
    private Role role;

    @Column(name = "credential_reset_token", nullable = true)
    @Builder.Default
    private String resetToken = null;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
