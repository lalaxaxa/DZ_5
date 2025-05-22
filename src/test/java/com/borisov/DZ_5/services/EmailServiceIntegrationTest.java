package com.borisov.DZ_5.services;

import com.borisov.DZ_5.dto.EmailSendDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class EmailServiceIntegrationTest {

    @Container
    public static GenericContainer<?> mailHog = new GenericContainer<>("mailhog/mailhog")
            .withExposedPorts(1025, 8025);

    @DynamicPropertySource
    static void mailProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", mailHog::getHost);
        registry.add("spring.mail.port", () -> mailHog.getMappedPort(1025));
        registry.add("spring.mail.username", () -> "");
        registry.add("spring.mail.password", () -> "");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() throws Exception {
        EmailSendDTO dto = new EmailSendDTO();
        dto.setTo("test@localhost.com");
        dto.setSubject("Test Subject");
        dto.setText("Test body");

        emailService.sendEmail(dto);

        Thread.sleep(1000);

        String responseJson = getLatestMailHogMessages();

        assertThat(responseJson).contains("test@localhost.com");
        assertThat(responseJson).contains("Test Subject");
        assertThat(responseJson).contains("Test body");
    }

    private String getLatestMailHogMessages() throws Exception {
        String apiUrl = String.format("http://%s:%d/api/v2/messages",
                mailHog.getHost(),
                mailHog.getMappedPort(8025));

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertThat(responseCode).isEqualTo(200);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder responseBody = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBody.append(inputLine);
            }
            return responseBody.toString();
        }
    }
}
