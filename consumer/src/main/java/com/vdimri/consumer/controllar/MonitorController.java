package com.vdimri.consumer.controllar;

import com.vdimri.consumer.service.ConsumerMonitor;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class MonitorController {

    private ConsumerMonitor monitor;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String host;

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Value("${spring.kafka.consumer.group}")
    private String group;

    public MonitorController(ConsumerMonitor monitor) {
        this.monitor = monitor;
    }

    @ApiOperation(value = "Endpoint to get the details of kafka topic")
    @GetMapping(path= "/topic/detail", produces = "application/json")
    public Map<String,String> getTopicOffSetDetails() {
        Map<String,String> responseJson= new HashMap<String, String>();
        Map<TopicPartition, ConsumerMonitor.PartionOffsets> offsetsDetails
                = monitor.getConsumerGroupOffsets(host, topic, group);
        offsetsDetails.entrySet().stream().forEach(e->{
            responseJson.put("currentOffset-"+e.getValue().getPartion(), String.valueOf(e.getValue().getCurrentOffset()));
            responseJson.put("endOffset-"+e.getValue().getPartion(), String.valueOf(e.getValue().getEndOffset()));
            responseJson.put("partion-"+e.getValue().getPartion(), String.valueOf(e.getValue().getPartion()));
            responseJson.put("topic-"+e.getValue().getPartion(),e.getValue().getTopic());
        });
        return responseJson;
    }
}