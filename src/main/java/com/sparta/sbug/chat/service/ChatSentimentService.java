package com.sparta.sbug.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class ChatSentimentService {

    private final String CLIENT_KEY_ID = "jgfyvgs1kh";
    private final String CLIENT_KEY = "6RCTuhRmaRqrZuHpKVBXJrFvXwO3unkLmxWoLcET";

    public String callSentimentApi(String content) {
        Object sentiment = null;
        try {
            URI url = new URI("https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze");
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_KEY_ID);
            headers.set("X-NCP-APIGW-API-KEY", CLIENT_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("content", content);

            HttpEntity<?> request = new HttpEntity<>(body.toSingleValueMap(), headers);
            var response = restTemplate.postForObject(url, request, String.class);
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(response, Map.class);
            sentiment = ((Map) map.get("document")).get("sentiment");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (String) sentiment;
    }
}
