package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mms.demo.entity.Doctor;
import com.mms.demo.entity.Patient;
import com.mms.demo.entity.Report;
import com.mms.demo.repository.ReportRepository;
import com.mms.demo.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    private ReportRepository repo;

    @Override
    public Report createReport(Report report) {
        return repo.save(report);
    }

    @Override
    public void deleteReport(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Report> getReportByDoctor(Doctor doctor) {
        return repo.findAllByDoctor(doctor);
    }

    @Override
    public Optional<Report> getReportById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Report> getReportByStamp(LocalDateTime stamp) {
        return repo.findAllByStamp(stamp);
    }

    @Override
    public List<Report> getReportsByPatient(Patient patient) {
        return repo.findAllByPatient(patient);
    }

    @Override
    public Report updateReport(Long id, Report reportUpdates) {
        Optional<Report> temp = getReportById(id);

        if (temp.isEmpty()) {
            return null;
        }

        Report report = temp.get();
        report.setPatient(reportUpdates.getPatient());
        report.setReportText(reportUpdates.getReportText());
        report.setStamp(reportUpdates.getStamp());

        return repo.save(report);
    }
    
    
}
