package com.borisov.DZ_5.services;

import com.borisov.DZ_5.events.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListenerService {
    @KafkaListener(topics = "${topic.user-events}", containerFactory = "kafkaListenerContainerFactory")
    public void onUserEvent(UserEvent event) {
        String subject = "Уведомление от вашего сайта";
        String body = switch (event.getOperation()) {
            case CREATE -> "Здравствуйте! Ваш аккаунт на сайте успешно создан.";
            case DELETE -> "Здравствуйте! Ваш аккаунт был удалён.";
        };
        System.out.println(body);
        //emailSender.sendEmail(event.getEmail(), subject, body);
    }
}
