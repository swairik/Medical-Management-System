package com.mms.demo.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Slot;
import com.mms.demo.repository.AppointmentRepository;
import com.mms.demo.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appRepo;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        return appRepo.save(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        appRepo.deleteById(id);
        
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appRepo.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appRepo.findById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return appRepo.findAllByPatient(patient);
    }

    @Override
    public List<Appointment> getAppointmentsBySlot(Slot slot) {
        return appRepo.findAllBySlot(slot);
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointmentUpdates) {
        Optional<Appointment> temp = getAppointmentById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Appointment appointment = temp.get();
        appointment.setPatient(appointmentUpdates.getPatient());
        appointment.setSlot(appointmentUpdates.getSlot());
        

        return appRepo.save(appointment);
    }
    
    
}
