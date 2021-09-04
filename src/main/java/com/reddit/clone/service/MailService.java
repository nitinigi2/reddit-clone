package com.reddit.clone.service;

import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    @Autowired
    private final MailContentBuilder mailContentBuilder;
    @Autowired
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(NotificationEmail notificationEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jijona2286@mom2kid.com");
        message.setTo(notificationEmail.getRecipient());
        message.setSubject(notificationEmail.getSubject());
        message.setText(mailContentBuilder.build(notificationEmail.getBody()));
        try {
            mailSender.send(message);
            log.info("Activation email sent successfully.");
        } catch (MailException e) {
            log.error("Something went wrong while sending email::::", e.getMessage());
            throw new SpringRedditException(e.toString());
        }

    }
}
