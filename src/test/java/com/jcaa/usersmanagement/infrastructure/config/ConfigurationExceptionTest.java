package com.jcaa.usersmanagement.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConfigurationExceptionTest {

  @Test
  void becauseLoadFailed_keepsMessageAndCause() {
    final IllegalStateException cause = new IllegalStateException("boom");

    final ConfigurationException exception = ConfigurationException.becauseLoadFailed(cause);

    assertThat(exception)
        .hasMessage("Failed to load the application configuration.")
        .hasCause(cause);
  }
}