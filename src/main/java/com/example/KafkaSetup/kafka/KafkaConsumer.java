package com.example.KafkaSetup.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaConsumer {
    private final ObjectMapper objectMapper;

    public KafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @KafkaListener(topics = "example-topic", groupId = "my-consumer-group", concurrency = "3")
    public void listen(ConsumerRecord<String, String> record) {
        Iterable<Header> headers = record.headers();
        for (Header header : headers) {
            String headerKey = header.key();
            String headerValue = new String(header.value());
            System.out.println("Header: " + headerKey + " = " + headerValue);
        }
        try {
            String jsonData = record.value();
            Map<String, String> data = objectMapper.readValue(jsonData, Map.class);
            System.out.println("Received data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
