package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DatabaseConfigPostgresTest {

  @Test
  void shouldBuildPostgresUrlTranslatingRequiredSslMode() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig("postgres", "db.example.com", 5432, "postgres", "user", "secret", "REQUIRED");

    // Act
    final String jdbcUrl = config.buildJdbcUrl();

    // Assert
    assertThat(config.isPostgres()).isTrue();
    assertThat(jdbcUrl).isEqualTo("jdbc:postgresql://db.example.com:5432/postgres?sslmode=require");
  }

  @Test
  void shouldBuildPostgresUrlTranslatingDisabledSslMode() {
    // Arrange
    final DatabaseConfig config =
        new DatabaseConfig("postgres", "localhost", 5432, "crud_usuarios", "user", "secret", "DISABLED");

    // Act
    final String jdbcUrl = config.buildJdbcUrl();

    // Assert
    assertThat(jdbcUrl).isEqualTo("jdbc:postgresql://localhost:5432/crud_usuarios?sslmode=disable");
  }
}
