package com.example.vinicius.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    private final RestTemplate restTemplate;

    public EmailService(){
        this.restTemplate = new RestTemplate();
    }
}
