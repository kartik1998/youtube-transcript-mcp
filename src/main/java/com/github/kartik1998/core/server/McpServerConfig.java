package com.github.kartik1998.core.server;

import com.github.kartik1998.core.annotations.McpClass;
import com.github.kartik1998.core.annotations.McpParam;
import com.github.kartik1998.core.annotations.McpTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Configuration
public class McpServerConfig {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired private ApplicationContext applicationContext;

  @Bean
  public StdioServerTransportProvider transportProvider() {
    return new StdioServerTransportProvider(new ObjectMapper());
  }

  @Bean
  public McpSyncServer mcpServer(StdioServerTransportProvider transportProvider)
      throws ClassNotFoundException, JsonProcessingException {
    McpSyncServer server =
        McpServer.sync(transportProvider)
            .serverInfo("mcp-example-server", "1.0.0")
            .capabilities(
                McpSchema.ServerCapabilities.builder()
                    .resources(false, true) // Enable resource support
                    .tools(true) // Enable tool support
                    .prompts(true) // Enable prompt support
                    .logging() // Enable logging support
                    .build())
            .build();

    registerTools(server);

    return server;
  }

  private void registerTools(McpSyncServer server)
      throws ClassNotFoundException, JsonProcessingException {
    final ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(McpClass.class));
    for (BeanDefinition bd : scanner.findCandidateComponents("com.github.kartik1998.service")) {
      final Class<?> cls = Class.forName(bd.getBeanClassName());
      for (Method method : cls.getDeclaredMethods()) {
        McpTool toolAnnotation = method.getAnnotation(McpTool.class);
        if (toolAnnotation != null) {
          String name = toolAnnotation.name().isEmpty() ? method.getName() : toolAnnotation.name();
          String description =
              toolAnnotation.description().isEmpty()
                  ? method.getAnnotation(McpTool.class).description()
                  : toolAnnotation.description();
          Map<String, Object> schema = generateToolSchema(method);
          SyncToolSpecification toolSpec =
              new SyncToolSpecification(
                  new McpSchema.Tool(name, description, MAPPER.writeValueAsString(schema)),
                  (exchange, arguments) -> {
                    try {
                      Object bean = applicationContext.getBean(cls);
                      Parameter[] params = method.getParameters();
                      Object[] paramValues = new Object[params.length];
                      for (int i = 0; i < params.length; i++) {
                        McpParam annotation = params[i].getAnnotation(McpParam.class);
                        String paramName =
                            annotation != null && !annotation.name().isEmpty()
                                ? annotation.name()
                                : params[i].getName();
                        Object argValue = arguments.get(paramName);
                        if (argValue != null) {
                          paramValues[i] = MAPPER.convertValue(argValue, params[i].getType());
                        } else {
                          paramValues[i] = null;
                        }
                      }
                      Object result = method.invoke(bean, paramValues);
                      return new McpSchema.CallToolResult(
                          List.of(new TextContent(result.toString())), false);
                    } catch (Exception e) {
                      return new McpSchema.CallToolResult(
                          List.of(new TextContent(e.getMessage())), true);
                    }
                  });
          server.addTool(toolSpec);
        }
      }
    }
  }

  private Map<String, Object> generateToolSchema(Method method) {
    Map<String, Object> schema = new HashMap<>();
    schema.put("type", "object");

    Map<String, Object> properties = new HashMap<>();
    List<String> required = new ArrayList<>();

    for (Parameter param : method.getParameters()) {
      McpParam annotation = param.getAnnotation(McpParam.class);
      String paramName =
          annotation != null && !annotation.name().isEmpty() ? annotation.name() : param.getName();

      Map<String, Object> paramSchema = new HashMap<>();
      String type = getJsonType(param);
      paramSchema.put("type", type);
      if (type.equals("array")) {
        paramSchema.put("items", Map.of("type", extractItemsType(param)));
      }

      if (annotation != null && !annotation.description().isEmpty()) {
        paramSchema.put("description", annotation.description());
      }

      properties.put(paramName, paramSchema);

      if (annotation == null || annotation.required()) {
        required.add(paramName);
      }
    }

    schema.put("properties", properties);
    if (!required.isEmpty()) {
      schema.put("required", required);
    }

    return schema;
  }

  private String extractItemsType(Parameter param) {
    String str = param.getParameterizedType().getTypeName().toLowerCase();
    if (str.contains("string")) {
      return "string";
    } else if (str.contains("integer")) {
      return "integer";
    } else if (str.contains("double") || str.contains("float") || str.contains("long")) {
      return "number";
    } else if (str.contains("boolean")) {
      return "boolean";
    } else {
      return "object";
    }
  }

  private String getJsonType(Parameter param) {
    Class<?> type = param.getType();
    if (type == String.class) {
      return "string";
    } else if (type == Integer.class || type == int.class) {
      return "integer";
    } else if (type == Double.class
        || type == double.class
        || type == Float.class
        || type == float.class) {
      return "number";
    } else if (type == Boolean.class || type == boolean.class) {
      return "boolean";
    } else if (type.isArray() || Collection.class.isAssignableFrom(type)) {
      return "array";
    } else {
      return "object";
    }
  }
}
