package com.example.vinicius.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    private final RestTemplate restTemplate;

    public EmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendEmail(String to, String subject, String body){
        String url = "https://api.email-provider.com/send";
        restTemplate.postForEntity(url, body, String.class);
    }
}
