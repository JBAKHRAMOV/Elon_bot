package com.company.controller;

import com.company.Status.Status;
import com.company.TelegramBot;
import com.company.component.Components;
import com.company.entity.ElonDTO;
import com.company.repository.Repositorys;
import com.company.util.InlineButtonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class ControllerText {
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private Components components;
    @Autowired
    private ElonDTO elonDTO;
    @Autowired
    private Repositorys repositorys;

    //has text
    public void hasText(Message message, String text){
        SendMessage  sendMessage=new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));

        if("/start".equalsIgnoreCase(text)){
            sendMessage.setText("Assalomu alaykum botimizga hush kelibsiz!!!\n");
            InlineKeyboardButton button1 = InlineButtonUtil.button("\uD83D\uDCDCElon berish", "/elonBerish");
            InlineKeyboardButton button2 = InlineButtonUtil.button("\uD83D\uDCDCElonlarni  korish", "/elonlarnikorish");
            List<InlineKeyboardButton> row1 = InlineButtonUtil.row(button1,button2);
            List<List<InlineKeyboardButton>> rows1 = InlineButtonUtil.rowList(row1);
            InlineKeyboardMarkup markup = InlineButtonUtil.keyboard(rows1);
            sendMessage.setReplyMarkup(markup);
            telegramBot.sendMsg(sendMessage);
        }
        else if (components.status.equals(Status.TITLE)){
            components.status=Status.CONTENT;
            elonDTO.setTitile(text);
            sendMessage.setText("Contentni kiriting: ");
            telegramBot.sendMsg(sendMessage);
        }
        else if (components.status.equals(Status.CONTENT)){
            components.status=null;
            elonDTO.setContent(text);
            SendPhoto sendPhoto=new SendPhoto();
            sendPhoto.setChatId(String.valueOf(message.getChatId()));
            sendPhoto.setPhoto(new InputFile(elonDTO.getPhoto()));
            sendPhoto.setCaption(elonDTO.getTitile()+"\n"+elonDTO.getContent());
            telegramBot.sendMsg(sendPhoto);
            User user=message.getFrom();
            repositorys.create_card(elonDTO, Math.toIntExact(user.getId()),user.getFirstName(),user.getUserName());
            sendMessage.setText("elon joylandi");
            telegramBot.sendMsg(sendMessage);
        }
        else {
            sendMessage.setText("botni ishga tushirish uchun /start ni bosing");
        }

    }
    public void hasPhoto(Message message){
        components.status= Status.TITLE;
        elonDTO.setPhoto(message.getPhoto().get(message.getPhoto().size()-1).getFileId());
        System.out.println(message.getPhoto().get(message.getPhoto().size()-1));
        SendMessage sendMessage=new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("Titeni kiriting: ");
        telegramBot.sendMsg(sendMessage);
    }
}
