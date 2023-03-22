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

/**
 * Credential is an entity that primarily stores email/password pairs. It also stores flags for
 * specifying whether the credentials belong to a patient or a doctor, and also whether they have
 * admin privileges or not.
 * <p>
 * Supports a Builder() as well as a toBuilder(), which are the preferred methods of constructing
 * objects of this class.
 * 
 * @author Mananveer Singh
 * @author Swairik Dey
 */
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
    /**
     * The Primary Key. Autogenerated by the DBMS on the creation of an entry. While another
     * candidate key exists, identifying using a surrogate key when you already have the object is
     * far faster and easier to work with. Intended to be generated automatically by the Database
     * Management System.
     * 
     */
    @Id
    @Column(name = "credential_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The corresponding email.
     *
     * 
     */
    @Column(name = "credential_email", nullable = false, unique = true)
    private String email;

    /**
     * The encrypted password hash.
     *
     * 
     */
    @Column(name = "credential_password", nullable = false)
    private String password;

    /**
     * An enumerator that specifies the role of the bearer of this credential.
     * 
     * @see #Role
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "credential_role", nullable = false)
    private Role role;

    /**
     * Populates and returns a Credentials object after verifying authentications.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Get the attribute mapped to the username alias.
     * 
     * @return email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Checks if the account is not expired
     * 
     * @return true if valid else false
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the account is not locked.
     * 
     * @return false if expired else true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if the credentials stored in the object are not expired.
     * 
     * @return false if expired else true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the credentials are still enabled.
     * 
     * @return true if enabled else false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Get the attribute mapped to the password alias.
     * 
     * @return hashed password
     */
    @Override
    public String getPassword() {
        return password;
    }

}
