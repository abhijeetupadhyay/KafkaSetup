package com.example.KafkaSetup.controller;

import com.example.KafkaSetup.kafka.KafkaProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {
    private final KafkaProducer producer;

    public KafkaController(KafkaProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        // Prepare headers
        Map<String, String> headers = new HashMap<>();
        headers.put("userId", "12345");
        headers.put("operation", "CREATE");

        // Prepare the data as a JSON object
        Map<String, String> data = new HashMap<>();
        data.put("content", message);
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));

        producer.sendMessage("example-topic", headers, data);
        return "Message sent to Kafka: " + message;
    }
}
