package com.ghtkdb.journal.application.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendMail(String to, String subject, String body);
}
