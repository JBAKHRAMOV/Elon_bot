package com.company.component;


import com.company.Status.Status;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

@org.springframework.stereotype.Component
public class Components {
    public  Status status;
    public void log(User user, String text) {
        String str = String.format(LocalDateTime.now() + ",  userId: %d, firstName: %s, lastName: %s, text: %s", user.getId(), user.getFirstName(), user.getLastName(), text);
        System.out.println(str);
    }
}
