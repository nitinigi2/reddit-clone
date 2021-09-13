package com.reddit.clone.service;

import com.reddit.clone.config.RabbitMqConfig;
import com.reddit.clone.model.NotificationEmail;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqWithMailService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    public void send(NotificationEmail notificationEmail) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, notificationEmail);
    }
}
