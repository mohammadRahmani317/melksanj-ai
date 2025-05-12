package com.melksanj.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final RestTemplate restTemplate;
    private String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    @Value("${openai.api.key}")
    private String apiKey;


    public String getNeighborhoodInfo(String slug) {
        // درخواست به زبان طبیعی برای AI
        String prompt = String.format(
                "محله '%s' در تهران به فارسی چی میشه؟ و در کدوم منطقه شهرداریه؟ فقط نام فارسی و شماره منطقه رو بده.",
                slug
        );

        // ساخت JSON درخواست
        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", "openai/gpt-3.5-turbo");
        body.put("messages", Collections.singletonList(message));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // استخراج پاسخ
            Map<?, ?> choices = ((java.util.List<Map<?, ?>>) response.getBody().get("choices")).get(0);
            Map<?, ?> messageContent = (Map<?, ?>) choices.get("message");
            return messageContent.get("content").toString();

        } catch (Exception e) {
            return "خطا: " + e.getMessage();
        }
    }
}
