package com.example.onlineshopmvc.mailSender;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSenderImpl implements MailSender {

    @Override
    public void sendMail(String emailTo, String subject, String text) throws MessagingException {
        System.out.println("Email received");

        // Keys-Values-Behalter für die Email:
        Properties properties = new Properties();

        // Default properties für Gmail:
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols","TLSv1.2");

        // Login in Email (Config.Class):
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.EMAIL,Config.PASSWORD);
            }
        });

        // Email Nachricht vorbereiten:
        System.out.println("Preparing to send the Email  ..");
        Message message = prepareMessage(session,Config.EMAIL,emailTo,subject, text);

        // Email senden:
        Transport.send(message);
        System.out.println("Email has been sent successfully");
    }

    private Message prepareMessage(Session session, String emailFrom, String emailTo, String subject, String text) throws MessagingException {

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(emailFrom));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(emailTo));
            message.setSubject(subject);
            message.setText(text);

            return message;
        } catch (MessagingException e) {
            throw new MessagingException();
        }
    }
}