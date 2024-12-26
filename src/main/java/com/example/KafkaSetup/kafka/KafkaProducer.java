package com.example.KafkaSetup.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String topic, Map<String, String> headers, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, jsonData);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                record.headers().add(entry.getKey(), entry.getValue().getBytes());
            }
            kafkaTemplate.send(record);
            System.out.println("Message sent to Kafka topic: " + topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
