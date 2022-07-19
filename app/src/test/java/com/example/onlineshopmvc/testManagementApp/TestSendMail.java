package com.example.onlineshopmvc.testManagementApp;

import com.example.onlineshopmvc.mailSender.MailSender;
import com.example.onlineshopmvc.mailSender.MailSenderImpl;

import org.junit.Test;

import javax.mail.MessagingException;

public class TestSendMail {

    @Test
    public void testSendMail() throws MessagingException {

        MailSender mailSender = new MailSenderImpl();
        mailSender.sendMail("alwailiahmed95@gmail.com","Test Email","Hello World! ..");
    }
}
