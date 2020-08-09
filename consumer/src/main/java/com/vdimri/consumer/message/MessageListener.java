package com.vdimri.consumer.message;

import com.vdimri.consumer.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
