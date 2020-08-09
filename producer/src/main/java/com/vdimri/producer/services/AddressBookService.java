package com.vdimri.producer.services;

import com.vdimri.producer.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AddressBookService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public void publish(String address) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(KafkaHeaders.TOPIC, topicName);
        kafkaTemplate.send(new GenericMessage<String>(address, headers));
        log.info("Data - " + address.toString() + " sent to Kafka Topic - " + topicName);
    }
}
