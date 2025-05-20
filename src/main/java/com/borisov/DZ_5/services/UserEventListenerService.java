package com.borisov.DZ_5.services;

import borisov.core.UserChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListenerService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @KafkaListener(topics = "${topic.user-events}")
    public void onUserEvent(UserChangedEvent event) {
//        String subject = "Уведомление от вашего сайта";
//        String body = switch (event.getOperation()) {
//            case CREATE -> "Здравствуйте! Ваш аккаунт на сайте успешно создан.";
//            case DELETE -> "Здравствуйте! Ваш аккаунт был удалён.";
//        };
//        System.out.println(body);
        //emailSender.sendEmail(event.getEmail(), subject, body);
        LOGGER.info("Receive new event "+ event.getEmail());
    }
}
