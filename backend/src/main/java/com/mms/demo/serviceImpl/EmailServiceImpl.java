package com.mms.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.mms.demo.entity.Appointment;
import com.mms.demo.entity.Patient;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.EmailDetails;
import com.mms.demo.repository.AppointmentRepository;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.EmailService;
import com.mms.demo.service.ScheduleService;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;



    @Value("${spring.mail.username}")
    private String sender;

    @Value("${email.reminder.delay}")
    private Long minimumReminderDelay;


    @Autowired
    AppointmentRepository appointmentRepository;


    @Override
    @Async
    public void sendSimpleMail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMsgBody());

            javaMailSender.send(mailMessage);
            return;
        } catch (Exception e) {
            throw new CustomException("Error while sending mail", "EMAIL_NOT_SENT",
                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Scheduled(cron = "${email.reminder.interval}")
    @Async
    public void emailReminderScheduler() {
        List<Appointment> appointments = appointmentRepository.findAllByStartBetween(
                        LocalDateTime.now().plusHours(2),
                        LocalDateTime.now().plusHours(2).plusMinutes(30));
        final String subject = "Your Appointment is comping up!";
        for (Appointment appointment : appointments) {
            Patient patient = appointment.getPatient();

            String msgBody = String.format(
                            "Your appointment with Dr. %s is coming up!%n%n Appointment starts at: %s (roughly two hours from now)",
                            appointment.getDoctor().getName(), appointment.getStart());

            EmailDetails emailDetails = EmailDetails.builder().recipient(patient.getEmail())
                            .subject(subject).msgBody(msgBody).build();
            sendSimpleMail(emailDetails);
        }

    }

}
