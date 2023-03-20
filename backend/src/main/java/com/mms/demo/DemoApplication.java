package com.mms.demo;

import com.mms.demo.entity.*;
import com.mms.demo.entity.Role;
import com.mms.demo.service.*;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	PasswordEncoder encoder;

	@Autowired
    ReportService reportService;

	@Autowired
	CredentialService credentialService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecialityService specialityService;

    @Autowired
    PatientService patientService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    SlotService slotService;

    @Autowired
    AppointmentService appointmentService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	private String genAlnum(int targetStringLength) {
        int leftLimit = 48;
        int rightLimit = 122; 
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        
        return generatedString;
    }

	private void populateEntitiesFor(LocalDateTime temporalTarget, Integer entityScale) {
		temporalTarget = temporalTarget.truncatedTo(ChronoUnit.DAYS);
        
        // create patients
        for (int i = 0; i < entityScale; i++) {
            Patient temp = Patient.builder().age(25)
                                .email(genAlnum(14) + "@xyz.com")
                                .gender("M")
                                .name(genAlnum(10) + genAlnum(10))
                                .phone(genAlnum(10))
                                .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
                                .build();
            
            patientService.createPatient(temp);
            credentialService.createCredentials(Credential.builder().email(temp.getEmail()).password(encoder.encode("password")).role(Role.PATIENT).build());
        }

        // create doctors and specialities
        for (int i = 0; i < 10; i++) {
            Speciality speciality = Speciality.builder().name(genAlnum(7)).build();
            specialityService.createSpeciality(speciality);
        }

        List<Speciality> specialityList = specialityService.getAllSpecialities();
        for (int i = 0; i < entityScale / 10; i++) {
            Random rand = new Random();
            Doctor temp = Doctor.builder().age(25)
                                .email(genAlnum(14) + "@xyz.com")
                                .gender("M")
                                .name(genAlnum(10) + genAlnum(10))
                                .phone(genAlnum(10))
                                .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
                                .speciality(specialityList.get(rand.nextInt(specialityList.size())))
                                .build();
            
            doctorService.createDoctor(temp);
            credentialService.createCredentials(Credential.builder().email(temp.getEmail()).password(encoder.encode("password")).role(Role.DOCTOR).build());
        }

        // create slots, schedules, and appointments
        List<Doctor> doctorList = doctorService.getAllDoctors();
        List<Patient> patientList = patientService.getAllPatients();
        for (   ListIterator doctors = doctorList.listIterator(), patients = patientList.listIterator();
                doctors.hasNext() && patients.hasNext();
            ) {
            
            Doctor doctor = (Doctor)doctors.next();
            LocalTime incrementalStart = temporalTarget.toLocalTime()
                                                .withHour(9)
                                                .withMinute(0)
                                                .withSecond(0)
                                                .withNano(0);
            for (int i = 0; i < entityScale / 10 && patients.hasNext(); i++) {
                Patient patient = (Patient)patients.next();
                Slot slot = Slot.builder().start(incrementalStart)
                                .end(incrementalStart.plusMinutes(30))
                                .weekday(temporalTarget.getDayOfWeek())
                                .build();
                slotService.createSlot(slot);

                Schedule schedule = Schedule.builder().doctor(doctor)
                                        .slot(slot)
                                        .weekDate(temporalTarget.toLocalDate())
                                        .build();
                scheduleService.createSchedule(schedule);
                
                Appointment appointment = Appointment.builder()
                                            .patient(patient)
                                            .slot(slot)
                                            .stamp(temporalTarget.truncatedTo(ChronoUnit.SECONDS))
                                            .build();
                appointmentService.createAppointment(appointment);

                incrementalStart = incrementalStart.plusMinutes(30);
            }
        }

        reportService.forceRunReportGenerator(temporalTarget);
        
	}

    // @PostConstruct
	// private void populateDemoDatabase() {
	// 	credentialService.createCredentials(Credential.builder().email("admin@admin.com").role(Role.ADMIN).password(encoder.encode("admin")).build());
	// 	populateEntitiesFor(LocalDateTime.now().minusDays(3), 100);
	// 	// populateEntitiesFor(LocalDateTime.now().minusDays(2), 200);
	// 	// populateEntitiesFor(LocalDateTime.now().minusDays(1), 200);
	// }

}
