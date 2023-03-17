package com.mms.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SCHEDULE", schema = "MMSYSTEM")
@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(referencedColumnName = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(referencedColumnName = "slot_id", nullable = false)
    private Slot slot;

    @Column(name = "schedule_weekdate")
    private LocalDate weekDate;

    @Column(name = "schedule_approval", nullable = false)
    @Builder.Default
    private Boolean approval = false;

    public void setYear(Integer year) {
        weekDate = weekDate.withYear(year);
    }

    public void setWeek(Integer week) {
        weekDate = weekDate.withDayOfYear(1).plusWeeks(week);
    }

    public Integer getYear() {
        return weekDate.getYear();
    }

    public Integer getWeek() {
        return weekDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }
}
