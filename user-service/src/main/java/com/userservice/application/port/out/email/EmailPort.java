package com.userservice.application.port.out.email;

import com.userservice.application.port.out.email.emailStrategy.SendingEmailStrategy;

public interface EmailPort {

    void sendEmail(String to, SendingEmailStrategy strategy);
}
