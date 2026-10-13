package com.jcaa.usersmanagement.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jcaa.usersmanagement.infrastructure.adapter.email.BrevoConfig;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class BrevoSpringConfigTest {

    private final BrevoSpringConfig springConfig = new BrevoSpringConfig();

    private BrevoConfig buildConfig(final String apiKey) {
        return springConfig.brevoConfig(
                "https://api.brevo.com",
                apiKey,
                "remitente@test.local",
                "Gestion de Usuarios",
                Duration.ofSeconds(2),
                Duration.ofSeconds(5),
                3,
                Duration.ofMillis(500),
                2.0,
                50,
                10,
                5,
                Duration.ofSeconds(30));
    }

    @Test
    void brevoConfig_withValidValues_buildsConfig() {
        final BrevoConfig config = buildConfig("xkeysib-test-key");

        assertThat(config.baseUrl()).isEqualTo("https://api.brevo.com");
        assertThat(config.apiKey()).isEqualTo("xkeysib-test-key");
        assertThat(config.fromAddress()).isEqualTo("remitente@test.local");
        assertThat(config.retryMaxAttempts()).isEqualTo(3);
    }

    @Test
    void brevoConfig_withInvalidApiKeyPrefix_throwsException() {
        assertThatThrownBy(() -> buildConfig("clave-invalida"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void brevoRestClient_withValidConfig_buildsClient() {
        final RestClient client = springConfig.brevoRestClient(buildConfig("xkeysib-test-key"));

        assertThat(client).isNotNull();
    }
}