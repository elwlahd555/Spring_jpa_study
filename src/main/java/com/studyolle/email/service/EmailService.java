package com.studyolle.email.service;

import com.studyolle.repository.dto.EmailMessage;

public interface EmailService {
	
    void sendEmail(EmailMessage emailMessage);

}
