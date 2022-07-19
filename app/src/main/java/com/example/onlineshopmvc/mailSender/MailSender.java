package com.example.onlineshopmvc.mailSender;

import javax.mail.MessagingException;

public interface MailSender {

    /**
     * Send an email using JavaMailAPI
     * @param emailTo Recipient email
     * @param subject Email Title
     * @param text Email content
     * @throws MessagingException
     */
    void sendMail(String emailTo, String subject, String text) throws MessagingException;
}
