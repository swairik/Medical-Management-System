package com.mms.demo.serviceImpl;

import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Speciality;
import com.mms.demo.mapper.DataTransferObjectMapper;
import com.mms.demo.repository.AppointmentRepository;
import com.mms.demo.repository.DoctorRepository;
import com.mms.demo.repository.SpecialityRepository;
import com.mms.demo.service.AnalyticsService;
import com.mms.demo.transferobject.AdminAnalyticsDTO;
import com.mms.demo.transferobject.DoctorAnalyticsDTO;
import com.mms.demo.transferobject.SpecialityDTO;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private DataTransferObjectMapper<Speciality, SpecialityDTO> specialityMapper;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public AdminAnalyticsDTO getForAdmin() {
        AdminAnalyticsDTO.AdminAnalyticsDTOBuilder dtoBuilder = AdminAnalyticsDTO.builder();

        List<Speciality> specialities = specialityRepository.findAll();
        Map<SpecialityDTO, Long> specialityDoctorCount = new HashMap<>();
        Map<SpecialityDTO, Long> specialityPatientCount = new HashMap<>();
        for (Speciality speciality : specialities) {
            specialityDoctorCount.put(specialityMapper.entityToDto(speciality),
                            doctorRepository.countBySpeciality(speciality));

            specialityPatientCount.put(specialityMapper.entityToDto(speciality),
                            appointmentRepository.countByDoctor_Speciality(speciality));
        }
        dtoBuilder.specialityDoctorCount(specialityDoctorCount);
        dtoBuilder.specialityPatientCount(specialityPatientCount);

        LocalDate currentDate = LocalDate.now();
        LocalDate monthStart = currentDate.withDayOfMonth(1);
        LocalDate monthEnd = currentDate
                        .withDayOfMonth(currentDate.getMonth().length(currentDate.isLeapYear()));
        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek
                        .of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        final LocalDate weekStart =
                        LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek)); // first
        // day
        final LocalDate weekEnd = LocalDate.now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek)); // last
        // day
        HashSet<Long> uniqueDoctorsThisMonth = new HashSet<>();
        HashSet<Long> uniqueDoctorsThisWeek = new HashSet<>();
        HashSet<Long> uniqueDoctorsToday = new HashSet<>();
        Long appointmentsThisMonth = 0L;
        Long appointmentsThisWeek = 0L;
        Long appointmentsToday = 0L;
        for (LocalDate iTime = monthStart; !iTime.isAfter(monthEnd); iTime = iTime.plusDays(1)) {
            List<Appointment> appointments = appointmentRepository.findAllByStartBetween(
                            iTime.atStartOfDay(), iTime.atTime(LocalTime.MAX));
            appointmentsThisMonth += appointments.size();
            for (Appointment appointment : appointments) {
                uniqueDoctorsThisMonth.add(appointment.getDoctor().getId());
                if (iTime.isEqual(LocalDate.now())) {
                    uniqueDoctorsToday.add(appointment.getDoctor().getId());
                    appointmentsToday = Long.valueOf(appointments.size());
                }

                if (!(iTime.isBefore(weekStart) || iTime.isAfter(weekEnd))) {
                    uniqueDoctorsThisWeek.add(appointment.getDoctor().getId());
                    appointmentsThisWeek += appointments.size();
                }
            }
        }

        dtoBuilder.activeDoctorsThisMonth(Long.valueOf(uniqueDoctorsThisMonth.size()));
        dtoBuilder.activeDoctorsThisWeek(Long.valueOf(uniqueDoctorsThisWeek.size()));
        dtoBuilder.activeDoctorsToday(Long.valueOf(uniqueDoctorsToday.size()));
        dtoBuilder.appointmentsThisMonth(appointmentsThisMonth);
        dtoBuilder.appointmentsThisWeek(appointmentsThisWeek);
        dtoBuilder.appointmentsToday(appointmentsToday);

        return dtoBuilder.build();
    }

    @Override
    public Optional<DoctorAnalyticsDTO> getForDoctor(Long doctorID)
                    throws IllegalArgumentException {
        Optional<Doctor> fetchedContainer = doctorRepository.findById(doctorID);
        if (fetchedContainer.isEmpty()) {
            throw new IllegalArgumentException("Referenced doctor does not exist");
        }
        Doctor doctor = fetchedContainer.get();


        DoctorAnalyticsDTO.DoctorAnalyticsDTOBuilder dtoBuilder = DoctorAnalyticsDTO.builder();
        dtoBuilder.upcomingAppointments(Long.valueOf(appointmentRepository
                        .findAllByDoctorAndStartGreaterThanEqual(doctor, LocalDateTime.now())
                        .size()));
        dtoBuilder.patientCount(Long.valueOf(appointmentRepository.findAllByDoctor(doctor).size()));
        dtoBuilder.unfilledPrescriptions(appointmentRepository
                        .countByDoctorAndAppointmentDetails_PrescriptionNull(doctor));

        final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek
                        .of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        final LocalDate weekStart =
                        LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek)); // first
        // day
        final LocalDate weekEnd = LocalDate.now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek)); // last
        // day
        dtoBuilder.fulfilledAppointmentsThisWeek(Long.valueOf(appointmentRepository
                        .findAllByDoctorAndStartBetween(doctor, weekStart.atStartOfDay(),
                                        weekEnd.atTime(LocalTime.MAX))
                        .stream().filter(a -> a.getAttended()).count()));

        return Optional.of(dtoBuilder.build());
    }

}
