package com.example.telegram_botjob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageReceiver {

    private final SubscriberRepository subscriberRepository;
    private final TelegramJobBot telegramJobBot;

    @RabbitListener(queues = "vacanciesQueue")
    public void receiveMessage(String message) {

        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        try {
            JobVacancy jobVacancy = mapper.readValue(message, JobVacancy.class);
            String jobVacancyMessage = String.format("**Новая вакансия**\uD83D\uDCCC \n\nНазвание вакансии: %s \n" +
                            "Названии компании: %s \noffer:  %s \nОписание вакансии: %s \nСсылка на выкансию: %s",
                    jobVacancy.getVacancyName(), jobVacancy.getCompanyName(), jobVacancy.getOffer(),
                    jobVacancy.getDescription(), jobVacancy.getAlternateUrl());

            List<Subscriber> users = subscriberRepository.findAll();
            for (Subscriber user : users) {

                SendMessage sendMessage = new SendMessage(user.getChatId(), jobVacancyMessage);

                try {
                    telegramJobBot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
