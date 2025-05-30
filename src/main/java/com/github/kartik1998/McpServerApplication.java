package com.github.kartik1998;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class McpServerApplication {

  private static final Logger logger = LoggerFactory.getLogger(McpServerApplication.class);

  public static void main(String[] args) {
    try {
      logger.info("starting mcp server");
      SpringApplication.run(McpServerApplication.class, args);
    } catch (Exception e) {
      logger.error("Failed to start MCP Server", e);
      System.exit(1);
    }
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    try {
      logger.info("MCP Server is ready and listening for input");
    } catch (Exception e) {
      logger.error("Error in onApplicationReady", e);
      e.printStackTrace(System.err);
    }
  }
}
