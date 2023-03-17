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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode
@Entity
@Builder(toBuilder=true)
@ToString
@Table(name = "REPORT", schema = "MMSYSTEM")
public class Report {
    
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "report_timestamp")
    @Builder.Default
    private LocalDateTime stamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    @Lob
    @Column(name = "report_contents", length = 100000)
    private byte[] contents;
}
