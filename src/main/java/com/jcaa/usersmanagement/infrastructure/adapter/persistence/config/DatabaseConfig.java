package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import java.util.Locale;

public record DatabaseConfig(
    String type,
    String host,
    int port,
    String databaseName,
    String username,
    String password,
    String sslMode) {

  private static final String MYSQL_URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?sslMode=%s&serverTimezone=UTC&allowPublicKeyRetrieval=true";
  private static final String POSTGRES_URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s?sslmode=%s";
  private static final String TYPE_MYSQL = "mysql";
  private static final String TYPE_POSTGRES = "postgres";

  /** Constructor de compatibilidad: MySQL, igual que antes de agregar PostgreSQL. */
  public DatabaseConfig(
      final String host,
      final int port,
      final String databaseName,
      final String username,
      final String password,
      final String sslMode) {
    this(TYPE_MYSQL, host, port, databaseName, username, password, sslMode);
  }

  public boolean isPostgres() {
    return TYPE_POSTGRES.equalsIgnoreCase(type);
  }

  public String buildJdbcUrl() {
    if (isPostgres()) {
      return String.format(POSTGRES_URL_TEMPLATE, host, port, databaseName, postgresSslMode());
    }
    return String.format(MYSQL_URL_TEMPLATE, host, port, databaseName, sslMode);
  }

  /** Acepta los valores estilo MySQL (DISABLED, REQUIRED) y los traduce a PostgreSQL. */
  private String postgresSslMode() {
    final String normalized = sslMode == null ? "" : sslMode.trim().toLowerCase(Locale.ROOT);
    return switch (normalized) {
      case "", "disabled", "disable" -> "disable";
      case "required", "require" -> "require";
      default -> normalized;
    };
  }
}
