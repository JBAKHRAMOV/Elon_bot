package com.company;

import com.company.component.Components;
import com.company.controller.ControllerCallback;
import com.company.controller.ControllerText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private Components component;
    @Autowired
    private ControllerText controllerText;
    @Autowired
    private ControllerCallback controllerCallback;
    @Override
    public String getBotUsername() {
        return "@tez_elons_bot";
    }

    @Override
    public String getBotToken() {
        return "5279001370:AAEbEiBlYJUqo-XO8GpD67PHQ4RjIpxuPpU";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()){

            Message message=update.getMessage();
            User user=message.getFrom();

            if (message.hasText()){
                component.log(user,message.getText());
                controllerText.hasText(message,message.getText());
            }
            else if (message.hasPhoto()){
                controllerText.hasPhoto(message);
            }
        }
        else if (update.hasCallbackQuery()){
            Message message = update.getCallbackQuery().getMessage();
            User user = update.getCallbackQuery().getFrom();
            String text = update.getCallbackQuery().getData();
            component.log(user,text);
            controllerCallback.hasCallback(message,text);
        }
    }
    public void sendMsg(Object object) {
        try {
            if (object instanceof SendMessage) {
                execute((SendMessage) object);
            } else if (object instanceof EditMessageText) {
                execute((EditMessageText) object);
            } else if (object instanceof SendPhoto) {
                execute((SendPhoto) object);
            } else if (object instanceof SendVideo) {
                execute((SendVideo) object);
            } else if (object instanceof SendContact) {
                execute((SendContact) object);
            } else if (object instanceof SendLocation) {
                execute((SendLocation) object);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
