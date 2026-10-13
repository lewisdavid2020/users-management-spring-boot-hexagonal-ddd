package com.jcaa.usersmanagement.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DataSourceSpringConfigTest {

  @Test
  void dataSource_withUnreachableDatabase_failsToInitializePool() {
    final DataSourceSpringConfig config = new DataSourceSpringConfig();
    ReflectionTestUtils.setField(config, "dbType", "mysql");
    ReflectionTestUtils.setField(config, "dbHost", "127.0.0.1");
    ReflectionTestUtils.setField(config, "dbPort", 1);
    ReflectionTestUtils.setField(config, "dbName", "test_db");
    ReflectionTestUtils.setField(config, "dbUsername", "test_user");
    ReflectionTestUtils.setField(config, "dbPassword", "test_pass");
    ReflectionTestUtils.setField(config, "dbSslMode", "DISABLED");

    assertThatThrownBy(config::dataSource).isInstanceOf(RuntimeException.class);
  }
}