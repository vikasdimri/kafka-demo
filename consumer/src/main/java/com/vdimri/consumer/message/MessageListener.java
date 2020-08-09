package com.vdimri.consumer.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageListener {

    @KafkaListener(topics = "#{'${spring.kafka.topic.name}'.split('\\\\ ')}",
            groupId = "address-book")
    public void consume(String address) {
        log.info("Address :: {}", address);

    }

}
