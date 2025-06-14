package com.github.kartik1998.config;

import com.github.kartik1998.service.YoutubeTranscriberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public YoutubeTranscriberService accountService() {
    return new YoutubeTranscriberService();
  }
}
