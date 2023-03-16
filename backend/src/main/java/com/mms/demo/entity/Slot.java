package com.mms.demo.entity;


import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
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
@Table(name = "SLOT", schema = "MMSYSTEM")
public class Slot {
    @Id
    @Column(name = "slot_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "slot_weekday", nullable = false)
    private DayOfWeek weekday;

    @Column(name = "slot_start", nullable = false)
    @Temporal(TemporalType.TIME)
    private LocalTime start;

    @Column(name = "slot_end", nullable = false)
    @Temporal(TemporalType.TIME)
    private LocalTime end;

    @Column(name = "slot_capacity", nullable = false)
    @Min(1)
    @Builder.Default
    private Integer capacity = 1;
}
