package com.mms.demo.serviceImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.mms.demo.entity.Appointment;
import com.mms.demo.exception.CustomException;
import com.mms.demo.model.EmailDetails;
import com.mms.demo.service.AppointmentService;
import com.mms.demo.service.EmailService;
import com.mms.demo.service.ScheduleService;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    // @Autowired
    // private AppointmentService appointmentService;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${email.reminder.delay}")
    private Long minimumReminderDelay;

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
            throw new CustomException("Error while sending mail", "EMAIL_NOT_SENT");
        }
    }

    @Override
    @Scheduled(cron = "${email.reminder.interval}")
    @Async
    public void emailReminderScheduler() {

    }

}
