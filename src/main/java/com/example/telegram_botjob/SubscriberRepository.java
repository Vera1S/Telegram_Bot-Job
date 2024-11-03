package com.example.telegram_botjob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository <Subscriber, Long> {

     Subscriber findByChatId(String chatId);


}
