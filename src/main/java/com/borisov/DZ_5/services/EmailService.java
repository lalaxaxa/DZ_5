package com.borisov.DZ_5.services;

import com.borisov.DZ_5.dto.EmailSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailSendDTO emailSendDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@example.com");
        message.setTo(emailSendDTO.getTo());
        message.setSubject(emailSendDTO.getSubject());
        message.setText(emailSendDTO.getText());

        mailSender.send(message);
    }
}
