package com.mms.demo.service;

import com.mms.demo.model.EmailDetails;

public interface EmailService {
    public String sendSimpleMail(EmailDetails emailDetails);
}
