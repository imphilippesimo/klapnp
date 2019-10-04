package com.klapnp.core.notification.service.contract;

import com.klapnp.core.notification.NotificationContext;
import org.springframework.scheduling.annotation.Async;

/**
 * Interface use to push notifications out of the system.
 */
public interface NotificationService {

    void pushAccountCreation(NotificationContext notificationContext);

    /**
     * Push a notification to validate the account
     *
     * @param notificationContext : contains account owner's information
     */
    void pushAccountValidation(NotificationContext notificationContext);

    @Async
    void pushAccountPasswordReset(NotificationContext notificationContext);
}
