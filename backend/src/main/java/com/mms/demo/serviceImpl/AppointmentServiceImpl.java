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
    private AppointmentRepository repo;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        return repo.save(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        repo.deleteById(id);
        
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return repo.findAllByPatient(patient);
    }

    @Override
    public List<Appointment> getAppointmentsBySlot(Slot slot) {
        return repo.findAllBySlot(slot);
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
        

        return repo.save(appointment);
    }
    
    
}
