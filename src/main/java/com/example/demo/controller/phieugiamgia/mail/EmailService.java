package com.example.demo.controller.phieugiamgia.mail;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to,String subjects, String body) {
        SimpleMailMessage message= new SimpleMailMessage();
        message.setFrom("anhnhtph30259@fpt.edu.vn");
        message.setTo(to);
        message.setSubject(subjects);
        message.setText(body);
        javaMailSender.send(message);
    }

}
