package com.github.kartik1998.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class YtB2 {

  private static final Logger logger = LoggerFactory.getLogger(YtB2.class);

  @Value("${transcription.base.url}")
  private String transcriptionBaseUrl;

  private final RestTemplate restTemplate;

  @Autowired
  public YtB2(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String fetchTranscript(String youtubeUrl) {
    String requestUrl = transcriptionBaseUrl + "?url=" + youtubeUrl;
    logger.debug("request url={}", requestUrl);
    return restTemplate.getForObject(requestUrl, String.class);
  }
}
