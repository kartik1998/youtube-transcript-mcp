package com.github.kartik1998.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class YtB2 {

  @Value("${transcription.base.url}")
  private String transcriptionBaseUrl;

  private final RestTemplate restTemplate;

  @Autowired
  public YtB2(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String fetchTranscript(String youtubeUrl) {
    String requestUrl =
        transcriptionBaseUrl
            + "?url="
            + java.net.URLEncoder.encode(youtubeUrl, java.nio.charset.StandardCharsets.UTF_8);
    return restTemplate.getForObject(requestUrl, String.class);
  }
}
