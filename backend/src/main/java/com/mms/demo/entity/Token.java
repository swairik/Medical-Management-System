package com.mms.demo.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

@Entity
@Table(name = "TOKEN", schema = "MMSYSTEM")
@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="token_string", nullable = false)
    private String identifier;

    @Column(name="token_type", nullable = false)
    @Builder.Default
    private String type = "BEARER";

    @Column(name="reset_token_is_revoked", nullable = false)
    @Builder.Default
    private Boolean isRevoked = false;

    @Column(name="token_expiration_date", nullable = false)
    private LocalDateTime expirationStamp;

}
