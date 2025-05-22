package com.borisov.DZ_5.services;

import borisov.core.UserChangedEvent;
import com.borisov.DZ_5.dto.EmailSendDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListenerService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final EmailService emailService;
    @Autowired
    public UserEventListenerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${topic.user-events}")
    public void onUserEvent(UserChangedEvent event) {
        String subject = "Уведомление об изменении в учетной записи";
        String text = switch (event.getOperation()) {
            case CREATE -> "Здравствуйте! Ваш аккаунт на сайте успешно создан.";
            case DELETE -> "Здравствуйте! Ваш аккаунт был удалён.";
        };
        emailService.sendEmail(new EmailSendDTO(event.getEmail(), subject, text));
        LOGGER.info("Receive new event "+ event.getEmail());
    }
}
