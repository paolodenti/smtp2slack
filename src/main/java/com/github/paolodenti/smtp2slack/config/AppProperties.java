package com.github.paolodenti.smtp2slack.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application properties.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private SmtpProperties smtp = new SmtpProperties();
    private SlackProperties slack = new SlackProperties();

    @Data
    public static class SmtpProperties {

        private int port;
        private boolean details;
    }

    @Data
    public static class SlackProperties {

        private String webhook;
    }
}
