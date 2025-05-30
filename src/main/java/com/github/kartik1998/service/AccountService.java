package com.github.kartik1998.service;

import com.github.kartik1998.constants.McpConstants;
import com.github.kartik1998.core.annotations.McpClass;
import com.github.kartik1998.core.annotations.McpTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@McpClass
public class AccountService {

  private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

  public AccountService() {
  }

  @McpTool(
      name = McpConstants.GetAccountDetails.NAME,
      description = McpConstants.GetAccountDetails.DESCRIPTION)
  public Map<String, Object> getAccountDetails() {
    return null;
  }
}
