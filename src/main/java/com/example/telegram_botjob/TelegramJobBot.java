package com.example.telegram_botjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramJobBot extends TelegramLongPollingBot {

    private final SubscriberRepository subscriberRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        Message message = update.getMessage();

        if (message.hasText()) {
            String telegramText = message.getText();
            if (telegramText.equals("/a")) {
                Subscriber subscriberOld = subscriberRepository.findByChatId(String.valueOf(message.getChatId()));
                if (subscriberOld == null) {
                    Subscriber subscriber = new Subscriber();
                    subscriber.setChatId(String.valueOf(message.getChatId()));
                    subscriber.setUserName(message.getFrom().getUserName());

                    subscriberRepository.save(subscriber);
                    SendMessage sendMessage = new SendMessage(String.valueOf(message.getChatId()),
                            "Вы успешно подписались на рассылку вакансий");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    SendMessage sendMessage = new SendMessage(String.valueOf(message.getChatId()),
                            "Привет, " + subscriberOld.getUserName() + "! Вы уже являетесь подписчиком." );
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }

    }

    @Override
    public String getBotUsername() {
        return "hhJobBot_bot";
    }

    @Override
    public String getBotToken() {
        return "7508318427:AAGEhUDBD1yRLVQczRzC493T7WTiQ-79aSA";
    }
}
