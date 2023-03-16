package com.mms.demo.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Schedule;
import com.mms.demo.entity.Slot;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.AppointmentRequest;
import com.mms.demo.model.AppointmentResponse;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.DoctorService;
import com.mms.demo.service.PatientService;
import com.mms.demo.service.ScheduleService;
import com.mms.demo.service.SlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

        @Autowired
        PatientService patientService;

        @Autowired
        DoctorService doctorService;

        @Autowired
        SlotService slotService;

        @Autowired
        AppointmentService appointmentService;

        @Autowired
        ScheduleService scheduleService;

        @GetMapping("/display")
        public ResponseEntity<List<AppointmentResponse>> displayAllAppointments() {
                List<Appointment> appointments = appointmentService.getAllAppointments();
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> AppointmentResponse.createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/{id}")
        public ResponseEntity<AppointmentResponse> displayAppointmentById(@PathVariable Long id) {
                Appointment appointment = appointmentService.getAppointmentById(id)
                                .orElseThrow(() -> new CustomException("Appointment with given id not found",
                                                "APPOINTMENT_NOT_FOUND"));
                AppointmentResponse response = AppointmentResponse.createResponseFromAppointment(appointment);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/patient/{id}")
        public ResponseEntity<List<AppointmentResponse>> displayAppointmentsByPatient(
                        @PathVariable Long id) {
                Patient patient = patientService.getPatientById(id)
                                .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                "PATIENT_NOT_FOUND"));
                List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> AppointmentResponse.createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/slot/{id}")
        public ResponseEntity<List<AppointmentResponse>> displayAppointmentsBySlot(
                        @PathVariable Long id) {
                Slot slot = slotService.getSlotById(id)
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));
                List<Appointment> appointments = appointmentService.getAppointmentsBySlot(slot);
                List<AppointmentResponse> response = appointments.stream()
                                .map((a) -> AppointmentResponse.createResponseFromAppointment(a))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @GetMapping("/display/doctor/{id}/upcoming")
        public ResponseEntity<List<AppointmentResponse>> showUpcomingAppointmentsByDoctor(@PathVariable Long id) {
                Doctor doctor = doctorService.getDoctortById(id)
                                .orElseThrow(() -> new CustomException("Doctor with given id not found",
                                                "DOCTOR_NOT_FOUND"));

                // slots for a doctor scheduled between
                // appointments after a specific date (now)
                LocalDateTime temporalTargetStart = LocalDateTime.now();
                LocalDateTime temporalTargetEnd = LocalDateTime.now().withMonth(12).withDayOfMonth(31);
                List<Schedule> schedulesUpcoming = scheduleService.getSchedulesByDoctorAndWeekDay(doctor, temporalTargetStart.toLocalDate(), temporalTargetEnd.toLocalDate())
                Set<Slot> validSlots = new HashSet<>();
                for (Schedule sched : schedulesUpcoming) {
                        validSlots.add(sched.getSlot());
                }

                List<Appointment> validAppointments = new ArrayList<Appointment>();
                for (Slot slot : validSlots) {
                        validAppointments.addAll(appointmentService.getAppointmentsBySlot(slot).stream()
                        .filter(a   ->  a.getSlot().getStart().isAfter(temporalTargetStart.truncatedTo(ChronoUnit.SECONDS).toLocalTime()) &&
                                        a.getSlot().getEnd().isBefore(temporalTargetEnd.truncatedTo(ChronoUnit.SECONDS).toLocalTime())    )
                        .collect(Collectors.toList()));
                }



        }

        @PostMapping("/")
        public ResponseEntity<AppointmentResponse> createAppointment(
                        @Valid @RequestBody AppointmentRequest appointmentRequest) {
                Appointment appointment = createAppointmentFromRequest(appointmentRequest);

                List<Appointment> allAppointments = appointmentService.getAllAppointments();

                Appointment alreadyCreatedAppointment = allAppointments.stream()
                                .filter((a) -> (a.getPatient().getId() == appointmentRequest.getPatientId()
                                                && a.getSlot().getId() == appointmentRequest.getSlotId()))
                                .findFirst().orElse(null);

                if (alreadyCreatedAppointment != null) {
                        throw new CustomException("Appointment with given patient id and slot id already created",
                                        "APPOINTMENT_ALREADY_CREATED");
                }

                Appointment createdAppointment = appointmentService.createAppointment(appointment);

                Slot slot = slotService.getSlotById(appointmentRequest.getSlotId())
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));

                slot.setCapacity(slot.getCapacity() - 1);
                slotService.updateSlot(slot.getId(), slot);

                AppointmentResponse response = AppointmentResponse.createResponseFromAppointment(createdAppointment);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PutMapping("/{id}")
        public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Long id,
                        @Valid @RequestBody AppointmentRequest appointmentRequest) {
                Appointment appointment = createAppointmentFromRequest(appointmentRequest);
                Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
                AppointmentResponse response = AppointmentResponse.createResponseFromAppointment(updatedAppointment);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
                Appointment appointment = appointmentService.getAppointmentById(id)
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));

                appointmentService.deleteAppointment(id);

                Slot slot = appointment.getSlot();
                Integer maxCapacity = (int) slot.getStart().until(slot.getEnd(), ChronoUnit.MINUTES) / 30;
                slot.setCapacity(Integer.min(slot.getCapacity() + 1, maxCapacity));
                slotService.updateSlot(slot.getId(), slot);

                return new ResponseEntity<>(HttpStatus.OK);
        }

        public Appointment createAppointmentFromRequest(AppointmentRequest appointmentRequest) {
                Patient patient = patientService.getPatientById(appointmentRequest.getPatientId())
                                .orElseThrow(() -> new CustomException("Patient with given id not found",
                                                "PATIENT_NOT_FOUND"));
                Slot slot = slotService.getSlotById(appointmentRequest.getSlotId())
                                .orElseThrow(() -> new CustomException("Slot with given id not found",
                                                "SLOT_NOT_FOUND"));
                if (slot.getCapacity() == 0) {
                        throw new CustomException("No more capacity for this slot", "SLOT_CAPACITY_LIMIT_REACHED");
                }
                Appointment appointment = Appointment.builder()
                                .patient(patient)
                                .slot(slot)
                                .build();
                return appointment;
        }

}
