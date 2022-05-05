package com.company.controller;

import com.company.Status.Status;
import com.company.TelegramBot;
import com.company.component.Components;
import com.company.repository.Repositorys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ControllerCallback {
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private Components components;
    @Autowired
    private Repositorys repositorys;

    public void hasCallback(Message message,String text){
        SendMessage sendMessage=new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        if (text.equalsIgnoreCase("/elonBerish")){
            components.status= Status.PHOTO;
            sendMessage.setText("E'loningizga rasm joylashtirmoqchi bulsangiz rasm yuboring: ");
            telegramBot.sendMsg(sendMessage);
        }
        else if (text.equalsIgnoreCase("/elonlarnikorish")){
            repositorys.card_list(message);
        }
    }
}
