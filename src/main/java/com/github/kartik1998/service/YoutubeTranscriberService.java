package com.github.kartik1998.service;

import com.github.kartik1998.api.YtB2;
import com.github.kartik1998.constants.McpConstants;
import com.github.kartik1998.core.annotations.McpClass;
import com.github.kartik1998.core.annotations.McpParam;
import com.github.kartik1998.core.annotations.McpTool;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@McpClass
public class YoutubeTranscriberService {

  private static final Logger logger = LoggerFactory.getLogger(YoutubeTranscriberService.class);

  @Autowired YtB2 ytB2;

  @McpTool(
      name = McpConstants.YoutubeTranscriber.NAME,
      description = McpConstants.YoutubeTranscriber.DESCRIPTION)
  public Map<String, Object> getYoutubeVideoTranscription(
      @McpParam(
              name = McpConstants.YoutubeTranscriber.PARAM_NAME,
              description = McpConstants.YoutubeTranscriber.PARAM_DESCRIPTION)
          String youtubeVideoUrl) {
    try {
      String transcription = ytB2.fetchTranscript(youtubeVideoUrl);
      return Map.of("status", "success", "video_transcription", transcription);
    } catch (Exception e) {
      logger.error("Failed to get youtube video transcription");
      return Map.of("status", "failure", "error", e.getMessage());
    }
  }
}
