package com.mms.demo.service;

import com.mms.demo.model.EmailDetails;

/**
 * The Interface EmailService defines all the interactions needed between a high level controller
 * 
 * @author Swairik Dey
 */
public interface EmailService {
    /**
     * Sends an email.
     * 
     * @param emailDetails encapsulates all relevant details regarding the email
     */
    public void sendSimpleMail(EmailDetails emailDetails);

    void emailReminderScheduler();
}
