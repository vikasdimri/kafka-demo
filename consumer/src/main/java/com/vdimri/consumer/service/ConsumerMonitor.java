package com.vdimri.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class ConsumerMonitor {

    public static class PartionOffsets {
        private long endOffset;
        private long currentOffset;
        private int partion;
        private String topic;

        public PartionOffsets(long endOffset, long currentOffset, int partion, String topic) {
            this.endOffset = endOffset;
            this.currentOffset = currentOffset;
            this.partion = partion;
            this.topic = topic;
        }

        public long getEndOffset() {
            return endOffset;
        }

        public long getCurrentOffset() {
            return currentOffset;
        }

        public int getPartion() {
            return partion;
        }

        public String getTopic() {
            return topic;
        }
    }

    private final String monitoringConsumerGroupID = "monitoring_consumer_" + UUID.randomUUID().toString();

    public Map<TopicPartition, PartionOffsets> getConsumerGroupOffsets(String host, String topic, String groupId) {
        Map<TopicPartition, Long> logEndOffset = getLogEndOffset(topic, host);


        KafkaConsumer consumer = createNewConsumer(groupId, host);

        BinaryOperator<PartionOffsets> mergeFunction = (a, b) -> {
            throw new IllegalStateException();
        };

        Map<TopicPartition, PartionOffsets> result = logEndOffset.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> (entry.getKey()),
                        entry -> {
                            OffsetAndMetadata committed = consumer.committed(entry.getKey());
                            return new PartionOffsets(entry.getValue(), committed.offset(), entry.getKey().partition(), topic);
                        }, mergeFunction));


        return result;
    }

    public Map<TopicPartition, Long> getLogEndOffset(String topic, String host) {
        Map<TopicPartition, Long> endOffsets = new ConcurrentHashMap<>();
        KafkaConsumer<?, ?> consumer = createNewConsumer(monitoringConsumerGroupID, host);
        List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);
        List<TopicPartition> topicPartitions = partitionInfoList.stream().map(pi -> new TopicPartition(topic, pi.partition())).collect(Collectors.toList());
        consumer.assign(topicPartitions);
        consumer.seekToEnd(topicPartitions);
        topicPartitions.forEach(topicPartition -> endOffsets.put(topicPartition, consumer.position(topicPartition)));
        consumer.close();
        return endOffsets;
    }

    private static KafkaConsumer<?, ?> createNewConsumer(String groupId, String host) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new KafkaConsumer<>(properties);
    }
}