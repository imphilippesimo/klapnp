package com.klapnp.core.notification.service.impl;

import com.klapnp.core.notification.NotificationContext;
import com.klapnp.core.notification.service.contract.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Concrete implementation using Email as notifier.
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailNotificationService implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(MailNotificationService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final SpringTemplateEngine templateEngine;

    private final MessageSource messageSource;
    private JavaMailSender javaMailSender;

    public MailNotificationService(SpringTemplateEngine templateEngine, MessageSource messageSource, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void pushAccountCreation(NotificationContext notificationContext) {
        log.debug("Sending creation email to '{}'", notificationContext.getTo());
        sendEmailFromTemplate(notificationContext, "mail/accountCreationEmail", "email.creation.title");
    }

    @Override
    public void pushAccountValidation(NotificationContext notificationContext) {
        log.debug("Sending validation email to '{}'", notificationContext.getTo());
        sendEmailFromTemplate(
                notificationContext,
                "mail/klavActivationEmail",
                "klav.email.activation.title"
        );
    }

    @Async
    @Override
    public void pushAccountPasswordReset(NotificationContext notificationContext) {
        log.debug("Sending password reset email to '{}'", notificationContext.getTo());
        sendEmailFromTemplate(notificationContext, "mail/passwordResetEmail", "email.reset.title");
    }

    private void sendEmailFromTemplate(NotificationContext notificationContext, String templateName, String titleKey) {
        Context context = new Context(Locale.FRENCH);
        context.setVariable(USER, notificationContext.getEntity());
        context.setVariable(BASE_URL, "http://127.0.0.1:8080");
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, Locale.FRENCH);
        sendEmail(notificationContext.getTo(), subject, content, false, true);
    }

    @Async
    private void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom("klapnp@localhost");
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }
}
