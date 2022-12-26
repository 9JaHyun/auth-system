package com.userservice.application.port.out.email.emailStrategy;

import javax.mail.internet.MimeMessage;

@FunctionalInterface
public interface SendingEmailStrategy {

    void fillMessage(MimeMessage message);
}

