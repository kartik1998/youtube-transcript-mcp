package com.github.kartik1998;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@TestPropertySource(
    properties = {
      "spring.main.web-application-type=none",
      "spring.main.banner-mode=off",
      "logging.pattern.console=",
      "logging.level.org.springframework.ai=DEBUG",
      "logging.level.com.github.kartik1998=DEBUG"
    })
class McpServerTest {

  @Test
  void test() {
    //    assertNotNull(result);
    //    assertEquals("success", result.get("status"));
    //    var data = (List<String>) result.get("data");
    //    assertFalse(data.isEmpty());
    //    assertEquals("Security course", data.get(0));
  }
}
